package com.example.tikitrendingproject.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class QuantitySold(
    @PrimaryKey
    var productId: Int,
    var text: String,
    var value: Int
)