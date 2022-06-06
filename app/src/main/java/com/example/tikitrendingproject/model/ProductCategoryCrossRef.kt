package com.example.tikitrendingproject.model

import androidx.room.Entity

@Entity(
    primaryKeys = ["productId", "categoryId"]
)
data class ProductCategoryCrossRef(
    var productId: Int,
    var categoryId: Int
)