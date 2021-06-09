package ua.yandex.jere184.c4tappydefender.repository.impl

import kotlinx.coroutines.flow.Flow
import ua.yandex.jere184.c4tappydefender.db.userRecords.IUserRecordsDao
import ua.yandex.jere184.c4tappydefender.db.userRecords.UserRecordEntity
import ua.yandex.jere184.c4tappydefender.repository.IUserRecordRepository
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