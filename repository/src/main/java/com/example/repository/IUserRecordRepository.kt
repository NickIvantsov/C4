package com.example.repository

import kotlinx.coroutines.flow.Flow
import com.example.model.UserRecordEntity

interface IUserRecordRepository {
    val plantsFlow: Flow<List<com.example.model.UserRecordEntity>>

    fun insert(record: com.example.model.UserRecordEntity)

    fun delete(record: com.example.model.UserRecordEntity)
}