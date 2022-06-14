package com.example.tikitrendingproject.room.dao

import androidx.room.*
import com.example.tikitrendingproject.model.Image
import com.example.tikitrendingproject.util.Constant
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDao {
    @Query("SELECT * FROM Image")
    fun getAll(): Flow<List<Image>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(image: Image)

//    @Transaction
//    @Insert
//    suspend fun insert(images: List<Image>): Long

    @Update
    suspend fun update(image: Image): Int

    @Delete
    suspend fun delete(image: Image): Int

    @Query("DELETE FROM Image")
    suspend fun deleteAll()
}