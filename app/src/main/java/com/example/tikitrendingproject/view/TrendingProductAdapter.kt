package com.example.tikitrendingproject.view

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tikitrendingproject.databinding.ProductRowBinding
import com.example.tikitrendingproject.model.Product
import java.util.concurrent.Executors

class TrendingProductAdapter
    : ListAdapter<Product, TrendingProductAdapter.MyViewHolder>(
    AsyncDifferConfig.Builder<Product>(ProductDiffUtil())
        .setBackgroundThreadExecutor(Executors.newSingleThreadExecutor())
        .build()
    ){

    class MyViewHolder(var binding: ProductRowBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: Product?) {
            binding.product = item
            binding.widthOfScreen = 200
            binding.executePendingBindings()

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(ProductRowBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class ProductDiffUtil: DiffUtil.ItemCallback<Product>(){
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        TODO("Not yet implemented")
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        TODO("Not yet implemented")
    }

}