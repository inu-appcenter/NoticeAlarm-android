package org.jik.notification_proto.college

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(CollegeEntity::class),version = 1)
abstract class CollegeDatabase : RoomDatabase() {
    abstract fun collegeDAO() : CollegeDAO

    companion object {
        var INSTANCE : CollegeDatabase? = null

        fun getInstance(context : Context) : CollegeDatabase? {
            if(INSTANCE == null){
                synchronized(CollegeDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                    CollegeDatabase::class.java,"college.db")
                            .fallbackToDestructiveMigration()
                            .build()
                }
            }
            return INSTANCE
        }
    }
}