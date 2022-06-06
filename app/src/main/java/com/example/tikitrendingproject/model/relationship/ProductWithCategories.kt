package com.example.tikitrendingproject.model.relationship

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.tikitrendingproject.model.Product
import com.example.tikitrendingproject.model.ProductCategory
import com.example.tikitrendingproject.model.ProductCategoryCrossRef


data class ProductWithCategories (
    @Embedded var product: Product,
    @Relation(
        parentColumn = "productSku",
        entityColumn = "categoryId",
        associateBy = Junction(ProductCategoryCrossRef::class)
    )
    var categories: List<ProductCategory>
)