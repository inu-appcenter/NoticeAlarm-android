package org.jik.notification_proto.keyword

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "keyword")
data class KeywordEntity(
    @PrimaryKey(autoGenerate = true)
    var id : Long?,
    var keyword : String = "")