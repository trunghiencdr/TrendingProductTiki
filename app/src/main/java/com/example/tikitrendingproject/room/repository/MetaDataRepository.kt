package com.example.tikitrendingproject.room.repository

import com.example.tikitrendingproject.model.MetaData
import com.example.tikitrendingproject.room.dao.MetaDataDao
import kotlinx.coroutines.flow.Flow

class MetaDataRepository constructor(private val dao: MetaDataDao){
    val metaDataFlow: Flow<MetaData>
    get() = dao.getAll()

    suspend fun insert(item: MetaData?): Long{
        return dao.insert(item)
    }

     suspend fun findMetaDataByType(type: String): List<MetaData>{
        return dao.findMetaDataByTitle(type)
    }
}