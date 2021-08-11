package org.jik.notification_proto.college

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "college")
data class CollegeEntity(
    @PrimaryKey(autoGenerate = true)
    var id : Long?,
    var college : String = "")