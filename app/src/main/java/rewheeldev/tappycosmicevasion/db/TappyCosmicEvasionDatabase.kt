package rewheeldev.tappycosmicevasion.db

import androidx.room.Database
import androidx.room.RoomDatabase
import rewheeldev.tappycosmicevasion.db.userRecords.IUserRecordsDao
import com.example.model.UserRecordEntity

@Database(
    entities = [com.example.model.UserRecordEntity::class],
    version = 1, exportSchema = false
)
abstract class TappyCosmicEvasionDatabase : RoomDatabase() {
    abstract val userRecordsDao: IUserRecordsDao
}