package ua.yandex.jere184.c4tappydefender.repository

import kotlinx.coroutines.flow.Flow
import ua.yandex.jere184.c4tappydefender.db.userRecords.UserRecordEntity

interface IUserRecordRepository {
    val plantsFlow: Flow<List<UserRecordEntity>>

    fun insert(record: UserRecordEntity)

    fun delete(record: UserRecordEntity)
}