package com.example.tikitrendingproject.view

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tikitrendingproject.databinding.ProductRowBinding
import com.example.tikitrendingproject.model.Product

class TrendingProductAdapter
    : ListAdapter<Product, TrendingProductAdapter.MyViewHolder>(DiffUtilProduct()){

    class MyViewHolder(var binding: ProductRowBinding): RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}

class DiffUtilProduct: DiffUtil.ItemCallback<Product>(){
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        TODO("Not yet implemented")
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        TODO("Not yet implemented")
    }

}