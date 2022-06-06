package com.example.tikitrendingproject.model

import androidx.room.*
import com.google.gson.annotations.SerializedName

@Entity(
indices = [
    Index(value = ["name", "price"], unique = false)
])
data class Product(
    @PrimaryKey
    var id: Int,
    var name: String,
    @SerializedName("original_price")
    var originalPrice: Int,
    var price: Int,
    @SerializedName("quantity_sold")
    @Ignore
    var quantitySold: QuantitySold?=null,
    @SerializedName("rating_average")
    var ratingAverage: Float,
    @SerializedName("short_description")
    var shortDescription: String,
    @SerializedName("thumbnail_url")
    var thumbnailUrl: String,
)
 {
    constructor(
        id: Int,
        name: String,
        originalPrice: Int,
        price: Int,
        ratingAverage: Float,
        shortDescription: String,
        thumbnailUrl: String
    ) :
            this(
                id,
                name,
                originalPrice,
                price,
                null,
                ratingAverage,
                shortDescription,
                thumbnailUrl
            )
}