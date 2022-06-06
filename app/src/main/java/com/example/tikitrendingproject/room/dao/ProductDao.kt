package com.example.tikitrendingproject.room.dao

import androidx.room.*
import com.example.tikitrendingproject.model.Product
import com.example.tikitrendingproject.view.ProductCategoryDiffUtil
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Query("SELECT * FROM Product")
    fun getAll(): Flow<List<Product>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: Product?): Long

    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(products: ArrayList<Product>?): List<Long>

    @Update
    suspend fun update(product: Product): Int

    @Delete
    suspend fun delete(product: Product): Int


}