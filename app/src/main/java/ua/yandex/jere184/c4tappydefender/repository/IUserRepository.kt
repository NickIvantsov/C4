package ua.yandex.jere184.c4tappydefender.repository

import kotlinx.coroutines.Deferred
import ua.yandex.jere184.c4tappydefender.model.User

interface IUserRepository {
    fun save(key: String, user: User)
    suspend fun readAsync(key: String): Deferred<User>
}