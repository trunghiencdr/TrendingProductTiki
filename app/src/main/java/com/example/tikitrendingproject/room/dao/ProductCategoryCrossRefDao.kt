package com.example.tikitrendingproject.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.tikitrendingproject.model.ProductCategoryCrossRef

@Dao
interface ProductCategoryCrossRefDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ProductCategoryCrossRef?): Long
}