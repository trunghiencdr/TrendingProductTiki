package com.example.tikitrendingproject.model.relationship

import androidx.room.Embedded
import androidx.room.Relation
import com.example.tikitrendingproject.model.Product
import com.example.tikitrendingproject.model.QuantitySold

data class ProductWithQuantitySold(
    @Embedded var product: Product,
    @Relation(
            parentColumn = "id",
            entityColumn = "productId"
    )
    var quantitySold: QuantitySold
)