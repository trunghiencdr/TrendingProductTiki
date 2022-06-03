package com.example.tikitrendingproject.view

import android.text.Layout
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tikitrendingproject.databinding.ProductCategoryRowBinding
import com.example.tikitrendingproject.model.Product
import com.example.tikitrendingproject.model.ProductCategory
import java.util.concurrent.Executors

class ProductCategoryAdapter:
ListAdapter<ProductCategory, ProductCategoryAdapter.MyViewHolder>(
    AsyncDifferConfig.Builder<ProductCategory>(ProductCategoryDiffUtil())
    .setBackgroundThreadExecutor(Executors.newSingleThreadExecutor())
    .build()){
    class MyViewHolder(private val binding: ProductCategoryRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ProductCategory?) {
            binding.productCategory = item
            binding.executePendingBindings()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ProductCategoryRowBinding.inflate(LayoutInflater.from(parent.context))
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class ProductCategoryDiffUtil : DiffUtil.ItemCallback<ProductCategory>(){
    override fun areItemsTheSame(oldItem: ProductCategory, newItem: ProductCategory): Boolean {
        return oldItem.categoryId == newItem.categoryId
    }

    override fun areContentsTheSame(oldItem: ProductCategory, newItem: ProductCategory): Boolean {
        return oldItem == newItem
    }

}