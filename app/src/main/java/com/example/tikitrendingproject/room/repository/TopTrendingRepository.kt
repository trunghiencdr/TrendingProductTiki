package com.example.tikitrendingproject.room.repository

import android.content.Context
import androidx.room.Transaction
import com.example.tikitrendingproject.model.*
import com.example.tikitrendingproject.model.relationship.CategoryWithImages
import com.example.tikitrendingproject.model.relationship.CategoryWithProducts
import com.example.tikitrendingproject.room.DatabaseBuilder
import com.example.tikitrendingproject.room.dao.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    suspend fun findMetaDataByType(type: String): List<MetaData> {
        return metaDataDao.findMetaDataByTitle(type)
    }

    @Transaction
    suspend fun insertProductCategory(item: ProductCategory?){
        CoroutineScope(Dispatchers.IO).launch{
            var result = categoryDao.insert(item)
            if(result>0){
                // insert image of each category
                item?.images?.forEach {
                        it -> imageDao.insert(Image(it, item.categoryId))
                }
            }
        }
    }

    suspend fun getAllProductCategory(): List<CategoryWithImages> {
        return categoryDao.getAll()
    }

    suspend fun insertProducts(items: List<Product>?): List<Long> {
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

    suspend fun getCategoryWithProducts(): List<CategoryWithProducts> {
        return productCategoryCrossRefDao.getCategoryWithProducts()
    }

    suspend fun findByCategoryId(id: Int): List<ProductCategoryCrossRef> {
        return productCategoryCrossRefDao.findByCategoryId(id)
    }




    suspend fun findProductsBySku(sku: String): Product {
        return productDao.findBySku(sku)
    }

    suspend fun findQuantitySoldByProductSku(sku: String): QuantitySold {
        return quantitySoldDao.findByProductSku(sku)
    }




}