package com.example.tikitrendingproject.model

import com.google.gson.annotations.SerializedName

data class Data (
    @SerializedName("meta_data")
    val metaData: MetaData,
    val data: ArrayList<Product>,
    @SerializedName("next_page")
    val nextPage: String
    )