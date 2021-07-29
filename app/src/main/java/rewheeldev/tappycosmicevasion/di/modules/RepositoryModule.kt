package rewheeldev.tappycosmicevasion.di.modules

import com.example.repository.*
import com.example.repository.impl.*
import dagger.Binds
import dagger.Module
import com.example.feature_game.repository.IMeteoriteRepository
import com.example.feature_game.repository.MeteoriteRepositoryInMemoryImpl
import rewheeldev.tappycosmicevasion.repository.UserRecordRepositoryImpl
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
    abstract fun meteoriteRepositoryInMemoryRepository(repo: com.example.feature_game.repository.MeteoriteRepositoryInMemoryImpl): com.example.feature_game.repository.IMeteoriteRepository

    @Binds
    @Singleton
    abstract fun spaceDustRepositoryInMemoryRepository(repo: SpaceDustRepositoryInMemoryImpl): ISpaceDustRepository

    @Binds
    @Singleton
    abstract fun soundRepositoryRepository(repo: SoundRepositoryImpl): ISoundRepository
}