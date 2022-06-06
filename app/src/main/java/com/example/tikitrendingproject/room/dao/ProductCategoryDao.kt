package com.example.tikitrendingproject.room.dao

import androidx.room.*
import com.example.tikitrendingproject.model.ProductCategory
import com.example.tikitrendingproject.model.relationship.CategoryWithImages
import com.example.tikitrendingproject.model.relationship.CategoryWithProducts
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductCategoryDao {
    @Transaction
    @Query("SELECT * FROM ProductCategory")
    suspend fun getAll(): List<CategoryWithImages>






    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(category: ProductCategory?): Long

    @Update
    suspend fun update(category: ProductCategory?): Int
}