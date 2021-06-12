package rewheeldev.tappycosmicevasion.repository

import kotlinx.coroutines.flow.Flow
import rewheeldev.tappycosmicevasion.db.userRecords.UserRecordEntity

interface IUserRecordRepository {
    val plantsFlow: Flow<List<UserRecordEntity>>

    fun insert(record: UserRecordEntity)

    fun delete(record: UserRecordEntity)
}