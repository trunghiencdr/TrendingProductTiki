package com.example.tikitrendingproject.model

import com.google.gson.annotations.SerializedName

data class Product (
    val id: Int,
    val name: String,
    @SerializedName("original_price")
    val originalPrice: Int,
    val price: Int,
    @SerializedName("quantity_sold")
    val quantitySold: QuantitySold,
    @SerializedName("rating_average")
    val ratingAverage: Float,
    @SerializedName("short_description")
    val shortDescription: String,
    @SerializedName("thumbnail_url")
    val thumbnailUrl: String
    )