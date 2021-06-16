package rewheeldev.tappycosmicevasion.di

import dagger.Binds
import dagger.Module
import rewheeldev.tappycosmicevasion.repository.*
import rewheeldev.tappycosmicevasion.repository.impl.*
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

    @Binds
    @Singleton
    abstract fun spaceDustRepositoryInMemoryRepository(repo: SpaceDustRepositoryInMemoryImpl): ISpaceDustRepository

    @Binds
    @Singleton
    abstract fun soundRepositoryRepository(repo: SoundRepositoryImpl): ISoundRepository
}