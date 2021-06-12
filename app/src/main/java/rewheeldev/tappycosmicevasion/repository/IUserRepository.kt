package rewheeldev.tappycosmicevasion.repository

import kotlinx.coroutines.Deferred
import rewheeldev.tappycosmicevasion.model.User

interface IUserRepository {
    fun save(key: String, user: User)
    suspend fun readAsync(key: String): Deferred<User>
}