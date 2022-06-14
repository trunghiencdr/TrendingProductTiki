package com.example.tikitrendingproject.room.repository

import com.example.tikitrendingproject.model.Image
import com.example.tikitrendingproject.room.dao.ImageDao
import com.example.tikitrendingproject.util.writeLogDebug
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ImageRepository(private val imageDao: ImageDao) {
    private val TAG = ImageRepository::class.java.canonicalName

    fun insert(image: Image) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                var result = 0
                try {
                    imageDao.insert(image)
                } catch (e: Exception) {
                    writeLogDebug("in class $TAG at imageDao.insert(image) with exception: ${e.localizedMessage}")
                }
                //update when item is conflict
                if (result < 0) {
                    try {
                        imageDao.update(image)
                    } catch (e: Exception) {
                        writeLogDebug("in class $TAG at imageDao.update(image) with exception: ${e.localizedMessage}")
                    }
                }

            }
        }
    }

    fun update(image: Image) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                var result = 0
                try {
                    imageDao.update(image)
                } catch (e: Exception) {
                    writeLogDebug("in class $TAG at imageDao.insert(image) with exception: ${e.localizedMessage}")
                }
            }
        }
    }

}