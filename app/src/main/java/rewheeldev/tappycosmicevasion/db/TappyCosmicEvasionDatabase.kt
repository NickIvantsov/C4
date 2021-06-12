package rewheeldev.tappycosmicevasion.db

import androidx.room.Database
import androidx.room.RoomDatabase
import rewheeldev.tappycosmicevasion.db.userRecords.IUserRecordsDao
import rewheeldev.tappycosmicevasion.db.userRecords.UserRecordEntity

@Database(
    entities = [UserRecordEntity::class],
    version = 1, exportSchema = false
)
abstract class TappyCosmicEvasionDatabase : RoomDatabase() {
    abstract val userRecordsDao: IUserRecordsDao
}