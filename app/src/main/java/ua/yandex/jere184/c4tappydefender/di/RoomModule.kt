package ua.yandex.jere184.c4tappydefender.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ua.yandex.jere184.c4tappydefender.db.TappyCosmicEvasionDatabase
import ua.yandex.jere184.c4tappydefender.db.userRecords.IUserRecordsDao
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