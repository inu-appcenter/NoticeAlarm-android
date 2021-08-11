package org.jik.notification_proto.keyword

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface KeywordDAO {

    @Insert(onConflict = REPLACE)
    fun insert(keyword : KeywordEntity)

    @Query("SELECT * FROM keyword")
    fun getAll() : List<KeywordEntity>

    @Delete
    fun delete(keyword: KeywordEntity)

    @Query("DELETE from keyword")
    fun deleteAll()
}





