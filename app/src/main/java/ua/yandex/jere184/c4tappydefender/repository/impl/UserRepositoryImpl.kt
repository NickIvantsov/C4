package ua.yandex.jere184.c4tappydefender.repository.impl

import android.content.SharedPreferences
import kotlinx.coroutines.*
import ua.yandex.jere184.c4tappydefender.model.User
import ua.yandex.jere184.c4tappydefender.repository.IUserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : IUserRepository {
    override fun save(key: String, user: User) {
        CoroutineScope(Dispatchers.IO).launch {
            val ed = sharedPreferences.edit()
            ed.putString(key, user.nickName)
            ed.apply()
        }
    }

    //= SAVED_TEXT
    override suspend fun read(key: String): Deferred<User> {
        return CoroutineScope(Dispatchers.IO).async {
            val result = sharedPreferences.getString(key, "")
            User(result)
        }
    }
}