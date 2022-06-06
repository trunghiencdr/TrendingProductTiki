package com.example.tikitrendingproject.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.ForeignKey.RESTRICT
import androidx.room.PrimaryKey

@Entity(tableName = "Image",
foreignKeys = [
    ForeignKey(
        entity = ProductCategory::class,
        parentColumns = arrayOf("categoryId"),
        childColumns = arrayOf("categoryId"),
        onUpdate = CASCADE,
        onDelete = RESTRICT
    )
])
data class Image(
    @PrimaryKey
    var url: String,
    var categoryId: Int
)