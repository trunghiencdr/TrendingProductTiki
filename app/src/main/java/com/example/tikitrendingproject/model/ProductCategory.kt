package com.example.tikitrendingproject.model

import com.google.gson.annotations.SerializedName

data class ProductCategory(
    @SerializedName("title")
    val title: String,
    @SerializedName("category_id")
    val categoryId: Int,
    @SerializedName("images")
    val images: List<String>
)