package com.example.tikitrendingproject.room.dao

import androidx.room.*
import com.example.tikitrendingproject.model.ProductCategoryCrossRef
import com.example.tikitrendingproject.model.relationship.CategoryWithProducts

@Dao
interface ProductCategoryCrossRefDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: ProductCategoryCrossRef?): Long

    @Update
    suspend fun update(item: ProductCategoryCrossRef?): Int

    @Transaction
    @Query("SELECT * FROM ProductCategory")
    suspend fun getCategoryWithProducts(): List<CategoryWithProducts>


    @Query("SELECT * FROM ProductCategoryCrossRef where categoryId = :id")
    suspend fun findByCategoryId(id: Int): List<ProductCategoryCrossRef>

}