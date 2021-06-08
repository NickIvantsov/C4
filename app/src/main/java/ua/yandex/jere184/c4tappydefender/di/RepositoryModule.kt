package ua.yandex.jere184.c4tappydefender.di

import dagger.Binds
import dagger.Module
import ua.yandex.jere184.c4tappydefender.repository.IUserRepository
import ua.yandex.jere184.c4tappydefender.repository.impl.UserRepositoryImpl
import javax.inject.Singleton

@Module
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun newsRepository(repo: UserRepositoryImpl?): IUserRepository
}