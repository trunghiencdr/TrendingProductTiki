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
        Product::class,
        MetaData::class,
        ProductCategoryCrossRef::class], version = 1
)
abstract class MyRoomDatabase: RoomDatabase(){
    abstract fun imageDao(): ImageDao
    abstract fun productCategoryDao(): ProductCategoryDao
    abstract fun productDao(): ProductDao
    abstract fun metaDataDao(): MetaDataDao
    abstract fun quantitySoldDao(): QuantitySoldDao
    abstract fun productCategoryCrossRefDao(): ProductCategoryCrossRefDao
}

//    companion object{
//        @Volatile
//        private var INSTANCE: AppDatabase? = null
//
//        private val MIGRATION_2_3 = object : Migration(2,3){
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("ALTER TABLE user ADD COLUMN name TEXT DEFAULT ''")
//                database.execSQL("ALTER TABLE user ADD COLUMN blog TEXT DEFAULT ''")
//                database.execSQL("ALTER TABLE user ADD COLUMN company TEXT DEFAULT ''")
//                database.execSQL("ALTER TABLE user ADD COLUMN created_at TEXT DEFAULT ''")
//                database.execSQL("ALTER TABLE user ADD COLUMN email TEXT DEFAULT '' ")
//                database.execSQL("ALTER TABLE user ADD COLUMN followers INTEGER DEFAULT -1 ")
//                database.execSQL("ALTER TABLE user ADD COLUMN bio TEXT DEFAULT ''")
//                database.execSQL("ALTER TABLE user ADD COLUMN location TEXT DEFAULT ''")
//            }
//        }
//        private val MIGRATION_3_4 = object : Migration(3,4){
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("ALTER TABLE user ADD COLUMN location TEXT DEFAULT ''")
//            }
//        }
//
//        fun getDatabase(context: Context): AppDatabase{
//            synchronized(this){
//                var instance = INSTANCE
//                if(instance==null){
//                    instance = Room.databaseBuilder(
//                        context,
//                        AppDatabase::class.java,
//                        DATABASE_NAME
//                    ).addMigrations(MIGRATION_2_3, MIGRATION_3_4)
//                        .build()
//                }
//                return instance
//            }
//        }
//    }