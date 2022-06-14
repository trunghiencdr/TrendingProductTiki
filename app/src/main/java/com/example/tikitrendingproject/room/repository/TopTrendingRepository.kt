package com.example.tikitrendingproject.room.repository

import android.content.Context
import android.util.Log
import androidx.room.Transaction
import com.example.tikitrendingproject.model.*
import com.example.tikitrendingproject.model.relationship.CategoryWithImages
import com.example.tikitrendingproject.model.relationship.CategoryWithProducts
import com.example.tikitrendingproject.room.DatabaseBuilder
import com.example.tikitrendingproject.room.dao.*
import com.example.tikitrendingproject.util.writeLogDebug
import com.example.tikitrendingproject.viewmodel.TrendingProductViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlin.concurrent.thread
import kotlin.properties.Delegates

class TopTrendingRepository(private val context: Context) {
    private lateinit var metaDataDao: MetaDataDao
    private lateinit var imageDao: ImageDao
    private lateinit var categoryDao: ProductCategoryDao
    private lateinit var productDao: ProductDao
    private lateinit var quantitySoldDao: QuantitySoldDao
    private lateinit var productCategoryCrossRefDao: ProductCategoryCrossRefDao
    public var TAG = TopTrendingRepository::class.java.canonicalName

    init {
        DatabaseBuilder.getInstance(context).let {
            metaDataDao = it.metaDataDao()
            imageDao = it.imageDao()
            categoryDao = it.productCategoryDao()
            productDao = it.productDao()
            quantitySoldDao = it.quantitySoldDao()
            productCategoryCrossRefDao = it.productCategoryCrossRefDao()
        }


    }

    fun insertMetaDataAndCategory(item: MetaData?) {
        insertMetaData(item)
        item?.items.let { categorys ->
            categorys?.forEach {
                insertProductCategory(it)
            }
        }
    }

    private fun insertMetaData(item: MetaData?) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    metaDataDao.insert(item)
                } catch (e: Exception) {
                    writeLogDebug("in class: $TAG fun: metaDataDao.insert(item) error: ${e.localizedMessage}")
                }
            }
        }
    }


    suspend fun findMetaDataByType(type: String): List<MetaData> {
        return metaDataDao.findMetaDataByTitle(type)
    }

    fun insertProductCategory(item: ProductCategory?) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                var result: Long = 0
                try {
                    result = categoryDao.insert(item)
                } catch (e: Exception) {
                    writeLogDebug("in class $TAG at categoryDao.insert(item) with exception:${e.localizedMessage} ")
                    return@withContext
                }
                //check if result =-1 (conflict primary key) then update item
                if (result < 0) {
                    try {
                        categoryDao.update(item)
                    } catch (e: Exception) {
                        writeLogDebug("in class $TAG at categoryDao.update(item) with exception:${e.localizedMessage} ")
                        return@withContext
                    }
                }

                // insert image of each category
                item?.images?.forEach { urlImage ->
//                    writeLogDebug("Test return fun")
                    insertImageForCategory(Image(urlImage, item.categoryId))
                }
            }
        }

    }

    private fun insertImageForCategory(image: Image) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                var result = 0
                try {
                    imageDao.insert(image)
                } catch (e: Exception) {
                    writeLogDebug("in class $TAG at imageDao.insert(image) with exception: ${e.localizedMessage}")
                }
                //update when item is conflict
                if(result<0){
                    try {
                        imageDao.update(image)
                    } catch (e: Exception) {
                        writeLogDebug("in class $TAG at imageDao.update(image) with exception: ${e.localizedMessage}")
                    }
                }

            }
        }
    }

    suspend fun getAllProductCategory(): List<CategoryWithImages> {
        return categoryDao.getAll()
    }

    suspend fun insertProduct(item: Product?): Long {
        return productDao.insert(item)
    }

    fun insertQuantitySold(quantitySold: QuantitySold?) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                var result = 0L
                try {
                    result = quantitySoldDao.insert(quantitySold)
                }catch (e: Exception){
                    writeLogDebug(TAG, "quantitySoldDao.insert(quantitySold)", e)
                }

                if (result < 0) {
                    try {
                         quantitySoldDao.update(quantitySold)
                    }catch (e: Exception){
                        writeLogDebug(TAG, "quantitySoldDao.update(quantitySold)", e)
                    }
                }
            }
        }
    }

    fun insertProductCategoryCrossRef(item: ProductCategoryCrossRef) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                var result = 0L
                try {
                    result = productCategoryCrossRefDao.insert(item)
                }catch (e: Exception){
                    writeLogDebug(TAG, "productCategoryCrossRefDao.insert(item)", e)
                }

                if (result < 0) {
                    try {
                         productCategoryCrossRefDao.update(item)
                    }catch (e: Exception){
                        writeLogDebug(TAG, "productCategoryCrossRefDao.insert(item)", e)
                    }
                }
            }
        }
    }

    suspend fun getCategoryWithProducts(): List<CategoryWithProducts> {
        return productCategoryCrossRefDao.getCategoryWithProducts()
    }

    suspend fun findByCategoryId(id: Int): List<ProductCategoryCrossRef> {
        return productCategoryCrossRefDao.findByCategoryId(id)
    }

    fun findProductsBySku(coroutineScope: CoroutineScope, sku: String): List<Product> {
        var products: List<Product> = ArrayList()
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    products = listOf(productDao.findBySku(sku))
                } catch (e: Exception) {
                    writeLogDebug("in class $TAG at productDao.findBySku(sku) with exception:${e.localizedMessage} ")
                }
            }
        }
        return products
    }


    suspend fun findProductsBySku(sku: String): Product {
        return productDao.findBySku(sku)
    }

    suspend fun findQuantitySoldByProductSku(sku: String): QuantitySold {
        return quantitySoldDao.findByProductSku(sku)
    }


}