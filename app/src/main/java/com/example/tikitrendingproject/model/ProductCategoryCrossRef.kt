package com.example.tikitrendingproject.model

import androidx.room.Entity

@Entity(
    primaryKeys = ["productSku", "categoryId"]
)
data class ProductCategoryCrossRef(
    var productSku: String,
    var categoryId: Int
)