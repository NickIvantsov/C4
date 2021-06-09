package ua.yandex.jere184.c4tappydefender.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ua.yandex.jere184.c4tappydefender.db.userRecords.IUserRecordsDao
import ua.yandex.jere184.c4tappydefender.db.userRecords.UserRecordEntity

@Database(
    entities = [UserRecordEntity::class],
    version = 1, exportSchema = false
)
abstract class TappyCosmicEvasionDatabase : RoomDatabase() {
    abstract val userRecordsDao: IUserRecordsDao
}