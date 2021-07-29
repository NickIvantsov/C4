package com.example.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.model.UserRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface IUserRecordsDao {
    @Query("SELECT * FROM user_records")
    fun getAllFlow(): Flow<List<UserRecordEntity>>

    @Insert
    fun insert(location: UserRecordEntity)

    @Delete
    fun delete(location: UserRecordEntity)
}