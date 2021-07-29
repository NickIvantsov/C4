package com.example.db.di

import android.app.Application
import androidx.room.Room
import com.example.db.TappyCosmicEvasionDatabase
import dagger.Module
import dagger.Provides
import com.example.db.dao.IUserRecordsDao
import javax.inject.Singleton


@Module
class RoomModule {

    @Singleton
    @Provides
    fun providesTappyCosmicEvasionDatabase(application: Application): TappyCosmicEvasionDatabase {
       return Room.databaseBuilder(
            application,
            TappyCosmicEvasionDatabase::class.java,
            "tappy-cosmic-evasion-db"
        ).build()
    }

    @Singleton
    @Provides
    fun providesProductDao(demoDatabase: TappyCosmicEvasionDatabase): IUserRecordsDao {
        return demoDatabase.userRecordsDao
    }
}