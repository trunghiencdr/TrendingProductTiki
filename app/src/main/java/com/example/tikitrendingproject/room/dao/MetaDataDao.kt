package com.example.tikitrendingproject.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.tikitrendingproject.model.MetaData
import kotlinx.coroutines.flow.Flow

@Dao

interface MetaDataDao {

    @Query("SELECT * FROM MetaData")
    fun getAll(): Flow<MetaData>


    @Insert(onConflict = IGNORE)
    suspend fun insert(metaData: MetaData?): Long

    @Query("SELECT * FROM MetaData where type = :type")
    suspend fun findMetaDataByTitle(type: String): List<MetaData>
}