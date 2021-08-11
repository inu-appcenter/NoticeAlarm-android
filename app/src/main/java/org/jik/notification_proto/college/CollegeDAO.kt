package org.jik.notification_proto.college

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface CollegeDAO {
    @Insert(onConflict = REPLACE)
    fun insert(college : CollegeEntity)

    @Query("SELECT * FROM college")
    fun getAll() : List<CollegeEntity>

    @Query("DELETE from college")
    fun deleteAll()
}