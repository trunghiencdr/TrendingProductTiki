package com.example.tikitrendingproject.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.tikitrendingproject.model.QuantitySold

@Dao
interface QuantitySoldDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: QuantitySold?): Long
}