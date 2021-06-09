package ua.yandex.jere184.c4tappydefender.db.userRecords

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_records")
data class UserRecordEntity(
    @ColumnInfo(name = "current_time")
    val currentTime: String,
    val dist: Float,
    val time: Long,
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
)