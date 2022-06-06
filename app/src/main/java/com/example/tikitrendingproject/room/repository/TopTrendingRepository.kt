package com.example.tikitrendingproject.room.repository

import android.content.Context
import androidx.room.Transaction
import com.example.tikitrendingproject.model.*
import com.example.tikitrendingproject.model.relationship.CategoryWithImages
import com.example.tikitrendingproject.room.DatabaseBuilder
import com.example.tikitrendingproject.room.dao.*
import kotlinx.coroutines.flow.Flow

class TopTrendingRepository(private val context: Context) {
    private lateinit var metaDataDao: MetaDataDao
    private lateinit var imageDao: ImageDao
    private lateinit var categoryDao: ProductCategoryDao
    private lateinit var productDao: ProductDao
    private lateinit var quantitySoldDao: QuantitySoldDao
    private lateinit var productCategoryCrossRefDao: ProductCategoryCrossRefDao
    init {
        val myRoomDatabase = DatabaseBuilder.getInstance(context).let {
            metaDataDao = it.metaDataDao()
            imageDao = it.imageDao()
            categoryDao = it.productCategoryDao()
            productDao = it.productDao()
            quantitySoldDao = it.quantitySoldDao()
            productCategoryCrossRefDao = it.productCategoryCrossRefDao()
        }


    }

    suspend fun insertMetaData(item: MetaData?): Long{
        return metaDataDao.insert(item)
    }

    fun findMetaDataByType(type: String): Flow<List<MetaData>> {
        return metaDataDao.findMetaDataByTitle(type)
    }

    @Transaction
    suspend fun insertProductCategory(item: ProductCategory?): Long{
        var result = categoryDao.insert(item)
        if(result>0){
            // insert image
            item?.images?.forEach {
                it -> imageDao.insert(Image(it, item.categoryId))
            }
            return 1
        }
        else return 0
    }

    fun getAllProductCategory(): Flow<List<CategoryWithImages>> {
        return categoryDao.getAll()
    }

    suspend fun insertProducts(items: ArrayList<Product>?): List<Long> {
        return productDao.insert(items)
    }

    suspend fun insertProduct(item: Product?): Long{
        return productDao.insert(item)
    }

    suspend fun insertQuantitySold(quantitySold: QuantitySold?): Long {
        return quantitySoldDao.insert(quantitySold)
    }

    suspend fun insertProductCategoryCrossRef(item: ProductCategoryCrossRef): Long {
        return productCategoryCrossRefDao.insert(item)
    }


}