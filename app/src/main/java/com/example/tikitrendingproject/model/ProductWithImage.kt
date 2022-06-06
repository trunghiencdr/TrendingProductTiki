package com.example.tikitrendingproject.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "ProductWithImage", primaryKeys = ["productId", "url"])
data class ProductWithImage (
    @ColumnInfo(name = "productId")
    var productId: Int,
    var url: String
)