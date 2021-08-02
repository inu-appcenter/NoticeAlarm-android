package org.jik.notification_proto.keyword

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(KeywordEntity::class), version = 1)
abstract class KeywordDatabase : RoomDatabase() {
    abstract fun keywordDAO() : KeywordDAO

    companion object {
        var INSTANCE : KeywordDatabase? = null

        fun getInstance(context : Context) : KeywordDatabase? {
            if(INSTANCE == null){
                synchronized(KeywordDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                    KeywordDatabase::class.java,"keyword.db")
                        .fallbackToDestructiveMigration().build()
                }
            }
            return INSTANCE
        }
    }
}