package com.example.tikitrendingproject.model.relationship

import androidx.room.Embedded
import androidx.room.Relation
import com.example.tikitrendingproject.model.Image
import com.example.tikitrendingproject.model.ProductCategory

data class CategoryWithImages(
    @Embedded var category: ProductCategory,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "categoryId"
    )
    var image: List<Image>
)