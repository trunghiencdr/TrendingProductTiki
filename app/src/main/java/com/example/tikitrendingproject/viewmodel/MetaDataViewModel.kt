package com.example.tikitrendingproject.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.tikitrendingproject.model.MetaData
import com.example.tikitrendingproject.room.repository.MetaDataRepository
import kotlinx.coroutines.*

class MetaDataViewModel constructor(private val metaDataRepository: MetaDataRepository):
ViewModel(){
    val metadataUsingFlow: LiveData<MetaData> = metaDataRepository.metaDataFlow.asLiveData()
    val message= MutableLiveData<String>()
    val success = MutableLiveData<Boolean>()
    suspend fun insert(item: MetaData){
       var job = CoroutineScope(Dispatchers.IO).launch {
            var result = metaDataRepository.insert(item)
            withContext(Dispatchers.Main){
                if(result>0){
                    success.postValue(true)
                    message.postValue("Insert successfully")
                }else{
                    success.postValue(false)
                    message.postValue("Insert failed")
                }
            }
        }
//        runBlocking {
//            job.cancelAndJoin()
//        }
    }
    fun findMetaDataByType(type: String): LiveData<List<MetaData>>{
        return metaDataRepository.findMetaDataByType(type).asLiveData()
    }
}