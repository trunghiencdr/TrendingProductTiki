package com.example.tikitrendingproject.room.dao

import androidx.room.*
import com.example.tikitrendingproject.model.QuantitySold

@Dao
interface QuantitySoldDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: QuantitySold?): Long

    @Update
    suspend fun update(item: QuantitySold?): Int


    @Query("SELECT * FROM QuantitySold where productSku = :sku")
    suspend fun findByProductSku(sku: String): QuantitySold
}