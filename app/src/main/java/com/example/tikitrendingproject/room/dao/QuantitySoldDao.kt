package com.example.tikitrendingproject.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tikitrendingproject.model.QuantitySold

@Dao
interface QuantitySoldDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: QuantitySold?): Long

    @Query("SELECT * FROM QuantitySold where productSku = :sku")
    suspend fun findByProductSku(sku: String): QuantitySold
}