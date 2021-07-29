package rewheeldev.tappycosmicevasion.db.userRecords

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface IUserRecordsDao {
    @Query("SELECT * FROM user_records")
    fun getAllFlow(): Flow<List<com.example.model.UserRecordEntity>>

    @Insert
    fun insert(location: com.example.model.UserRecordEntity)

    @Delete
    fun delete(location: com.example.model.UserRecordEntity)
}