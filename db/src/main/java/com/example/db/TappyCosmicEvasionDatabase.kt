package com.example.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.db.dao.IUserRecordsDao

@Database(
    entities = [com.example.model.UserRecordEntity::class],
    version = 1, exportSchema = false
)
abstract class TappyCosmicEvasionDatabase : RoomDatabase() {
    abstract val userRecordsDao: IUserRecordsDao
}