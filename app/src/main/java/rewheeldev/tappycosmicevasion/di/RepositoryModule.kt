package rewheeldev.tappycosmicevasion.di

import dagger.Binds
import dagger.Module
import rewheeldev.tappycosmicevasion.repository.IMeteoriteRepository
import rewheeldev.tappycosmicevasion.repository.IUserRecordRepository
import rewheeldev.tappycosmicevasion.repository.IUserRepository
import rewheeldev.tappycosmicevasion.repository.impl.MeteoriteRepositoryInMemoryImpl
import rewheeldev.tappycosmicevasion.repository.impl.UserRecordRepositoryImpl
import rewheeldev.tappycosmicevasion.repository.impl.UserRepositoryImpl
import javax.inject.Singleton

@Module
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun userRepository(repo: UserRepositoryImpl): IUserRepository

    @Binds
    @Singleton
    abstract fun userRecordsRepository(repo: UserRecordRepositoryImpl): IUserRecordRepository

    @Binds
    @Singleton
    abstract fun meteoriteRepositoryInMemoryRepository(repo: MeteoriteRepositoryInMemoryImpl): IMeteoriteRepository
}