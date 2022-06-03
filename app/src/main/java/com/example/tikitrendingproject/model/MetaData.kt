package com.example.tikitrendingproject.model

import com.google.gson.annotations.SerializedName

data class MetaData(
    @SerializedName("title")
    val title: String,
    @SerializedName("sub_title")
    val subTitle: String,
    @SerializedName("background_image")
    val backgroundImage: String,
    val items: ArrayList<ProductCategory>
)