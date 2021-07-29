package rewheeldev.tappycosmicevasion.repository.impl

import kotlinx.coroutines.flow.Flow
import com.example.db.dao.IUserRecordsDao
import rewheeldev.tappycosmicevasion.repository.IUserRecordRepository
import javax.inject.Inject

class UserRecordRepositoryImpl @Inject constructor(
    private val userRecordsDao: IUserRecordsDao
) : IUserRecordRepository {
    override val plantsFlow: Flow<List<com.example.model.UserRecordEntity>>
        get() = userRecordsDao.getAllFlow()

    override fun insert(record: com.example.model.UserRecordEntity) {
        userRecordsDao.insert(record)
    }

    override fun delete(record: com.example.model.UserRecordEntity) {
        userRecordsDao.delete(record)
    }
}