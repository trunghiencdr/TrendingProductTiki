package com.example.tikitrendingproject.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
@Entity
data class MetaData(
    @PrimaryKey
    var type: String,
    @SerializedName("title")
    var title: String,
    @SerializedName("sub_title")
    var subTitle: String,
    @SerializedName("background_image")
    var backgroundImage: String,
    @Ignore
    var items: List<ProductCategory>?
){
    constructor(
        type: String,
        title: String,
        subTitle: String,
        backgroundImage: String
    ): this(
        type,
        title,
        subTitle,
        backgroundImage,
        null
    )
}