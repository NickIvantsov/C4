package rewheeldev.tappycosmicevasion.repository.impl

import kotlinx.coroutines.flow.Flow
import rewheeldev.tappycosmicevasion.db.userRecords.IUserRecordsDao
import rewheeldev.tappycosmicevasion.db.userRecords.UserRecordEntity
import rewheeldev.tappycosmicevasion.repository.IUserRecordRepository
import javax.inject.Inject

class UserRecordRepositoryImpl @Inject constructor(
    private val userRecordsDao: IUserRecordsDao
) : IUserRecordRepository {
    override val plantsFlow: Flow<List<UserRecordEntity>>
        get() = userRecordsDao.getAllFlow()

    override fun insert(record: UserRecordEntity) {
        userRecordsDao.insert(record)
    }

    override fun delete(record: UserRecordEntity) {
        userRecordsDao.delete(record)
    }
}