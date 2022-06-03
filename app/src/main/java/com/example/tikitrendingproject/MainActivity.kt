package com.example.tikitrendingproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.tikitrendingproject.databinding.ActivityMainBinding
import com.example.tikitrendingproject.retrofit.RetroInstance
import com.example.tikitrendingproject.retrofit.repository.TrendingProductRepository
import com.example.tikitrendingproject.retrofit.service.TrendingProductService
import com.example.tikitrendingproject.viewmodel.TrendingProductViewModel
import com.example.tikitrendingproject.viewmodel.TrendingProductViewModelFactory

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var viewModel: TrendingProductViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModelCall()
        lifecycle.addObserver(viewModel)
        setUpBinding()

    }



     fun setUpBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.setVariable(BR.viewModel, viewModel)
        binding.executePendingBindings()
        binding.rvProductCategory.adapter = viewModel.productCategoryAdapter
    }


     fun getViewModelCall(): TrendingProductViewModel {
        val trendingProductRepository = TrendingProductRepository(RetroInstance.trendingProductService)
        val viewModelFactory = TrendingProductViewModelFactory(trendingProductRepository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(TrendingProductViewModel::class.java)
        viewModel.trendingProductCategory.observe(this, Observer {
            viewModel.setDataForCategory(it)
        })
         viewModel.callProductCategory(0,20)
        return viewModel

    }
}