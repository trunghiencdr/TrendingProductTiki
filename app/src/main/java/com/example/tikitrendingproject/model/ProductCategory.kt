package com.example.tikitrendingproject.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "ProductCategory")
data class ProductCategory(
    @SerializedName("title")
    var title: String,
    @SerializedName("category_id")
    @PrimaryKey
    var categoryId: Int,
    @SerializedName("images")
    @Ignore
    var images: ArrayList<String>?,

    // using for room
){
    constructor(
        title: String,
        categoryId: Int
    ): this(
        title,
        categoryId,
        null
    )
}