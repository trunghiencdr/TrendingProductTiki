package com.example.tikitrendingproject.room

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.tikitrendingproject.model.*
import com.example.tikitrendingproject.room.dao.*

@Database(
    entities = [
        Image::class,
        QuantitySold::class,
        ProductCategory::class,
        ProductWithImage::class,
        Product::class,
        MetaData::class,
        ProductCategoryCrossRef::class], version = 2
)
abstract class MyRoomDatabase: RoomDatabase(){
    abstract fun imageDao(): ImageDao
    abstract fun productCategoryDao(): ProductCategoryDao
    abstract fun productDao(): ProductDao
    abstract fun metaDataDao(): MetaDataDao
    abstract fun quantitySoldDao(): QuantitySoldDao
    abstract fun productCategoryCrossRefDao(): ProductCategoryCrossRefDao
}