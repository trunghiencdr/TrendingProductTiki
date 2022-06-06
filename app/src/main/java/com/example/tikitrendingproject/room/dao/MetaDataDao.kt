package com.example.tikitrendingproject.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.tikitrendingproject.model.MetaData
import kotlinx.coroutines.flow.Flow

@Dao

interface MetaDataDao {

    @Query("SELECT * FROM MetaData")
    fun getAll(): Flow<MetaData>


    @Insert(onConflict = REPLACE)
    suspend fun insert(metaData: MetaData?): Long

    @Query("SELECT * FROM MetaData where type = :type")
    fun findMetaDataByTitle(type: String): Flow<List<MetaData>>
}