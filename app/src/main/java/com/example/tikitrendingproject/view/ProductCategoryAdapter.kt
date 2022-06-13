package com.example.tikitrendingproject.view

import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tikitrendingproject.R
import com.example.tikitrendingproject.databinding.ProductCategoryRowBinding
import com.example.tikitrendingproject.model.Product
import com.example.tikitrendingproject.model.ProductCategory
import java.util.concurrent.Executors

class ProductCategoryAdapter(val action: Action<ProductCategory>) :
    ListAdapter<ProductCategory, ProductCategoryAdapter.MyViewHolder>(
        AsyncDifferConfig.Builder<ProductCategory>(ProductCategoryDiffUtil())
            .setBackgroundThreadExecutor(Executors.newSingleThreadExecutor())
            .build()
    ) {
    public var oldView: View? = null

    class MyViewHolder(private val binding: ProductCategoryRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(action: Action<ProductCategory>, item: ProductCategory?) {
            binding.productCategory = item
            binding.action = action
            binding.executePendingBindings()
        }

    }

    fun setChooseItem(itemIndex: Int) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ProductCategoryRowBinding.inflate(LayoutInflater.from(parent.context))
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (oldView == null) {
            oldView = holder.itemView
            holder.itemView.setBackgroundResource(R.drawable.bg_choosed_category)
        }
        holder.bind(action, getItem(position))
    }
}

class ProductCategoryDiffUtil : DiffUtil.ItemCallback<ProductCategory>() {
    override fun areItemsTheSame(oldItem: ProductCategory, newItem: ProductCategory): Boolean {
        return oldItem.categoryId == newItem.categoryId
    }

    override fun areContentsTheSame(oldItem: ProductCategory, newItem: ProductCategory): Boolean {
        return oldItem == newItem
    }

}