package com.example.tikitrendingproject.model.relationship

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.tikitrendingproject.model.Product
import com.example.tikitrendingproject.model.ProductCategory
import com.example.tikitrendingproject.model.ProductCategoryCrossRef

data class CategoryWithProducts (
    @Embedded var category: ProductCategory,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id",
        associateBy = Junction(ProductCategoryCrossRef::class)
    )
    var products: List<Product>
)