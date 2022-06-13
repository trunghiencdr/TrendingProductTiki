package com.example.tikitrendingproject

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.databinding.adapters.SearchViewBindingAdapter.setOnQueryTextListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.tikitrendingproject.databinding.ActivityMainBinding
import com.example.tikitrendingproject.retrofit.RetroInstance
import com.example.tikitrendingproject.retrofit.repository.TrendingProductRepository
import com.example.tikitrendingproject.retrofit.service.TrendingProductService
import com.example.tikitrendingproject.util.ItemMargin
import com.example.tikitrendingproject.util.SpacesItemDecoration
import com.example.tikitrendingproject.util.isNetworkAvailable
import com.example.tikitrendingproject.viewmodel.TrendingProductViewModel
import com.example.tikitrendingproject.viewmodel.TrendingProductViewModelFactory
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: TrendingProductViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = getViewModelCall()
        lifecycle.addObserver(viewModel)
        setUpBinding()

    }

    fun setUpBinding() {

        binding.setVariable(BR.viewModel, viewModel)
        binding.executePendingBindings()
        binding.rvProductCategory.adapter = viewModel.productCategoryAdapter
        binding.rvProduct.adapter = viewModel.productAdapter
        binding.rvProductCategory.addItemDecoration(ItemMargin(0, 0, 10, 0))
        binding.rvProduct.addItemDecoration(SpacesItemDecoration(10))
        viewModel.observerSearchView(binding.searchView)

    }




    fun getViewModelCall(): TrendingProductViewModel {
        val trendingProductRepository =
            TrendingProductRepository(RetroInstance.getService<TrendingProductService>(this))
        val viewModelFactory = TrendingProductViewModelFactory(trendingProductRepository, this)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(TrendingProductViewModel::class.java)
        viewModel.trendingProductCategory.observe(this, Observer {
            viewModel.setDataForCategory(it)
        })
        viewModel.trendingProduct.observe(this, Observer {
            viewModel.setDataForProduct(it)
        })
        if (isNetworkAvailable(this)) {
            viewModel.loadData(0, 20)
        } else {
            viewModel.callDataFromLocal(binding.root)
        }

        viewModel.getLoading().observe(this, Observer {
            if (it) {
                binding.progressBar.visibility = android.view.View.VISIBLE
            } else {
                binding.progressBar.visibility = android.view.View.GONE
            }
        })

        return viewModel

    }
}