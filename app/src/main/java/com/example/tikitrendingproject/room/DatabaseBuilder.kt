package com.example.tikitrendingproject.room

import android.content.Context
import androidx.room.Room

object DatabaseBuilder {

    private var INSTANCE: MyRoomDatabase? = null

    fun getInstance(context: Context): MyRoomDatabase {
        if (INSTANCE == null) {
            synchronized(MyRoomDatabase::class) {
                INSTANCE = buildRoomDB(context)
            }
        }
        return INSTANCE!!
    }

    private fun buildRoomDB(context: Context) =
        Room.databaseBuilder(
            context.applicationContext,
            MyRoomDatabase::class.java,
            "tiki_db"
        ).
        fallbackToDestructiveMigration().
        build()

}