package rewheeldev.tappycosmicevasion.repository.impl

import android.content.SharedPreferences
import kotlinx.coroutines.*
import com.example.model.User
import rewheeldev.tappycosmicevasion.repository.IUserRepository
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

    override suspend fun readAsync(key: String): Deferred<User> {
        return CoroutineScope(Dispatchers.IO).async {
            val result = sharedPreferences.getString(key, "")
            User(result)
        }
    }
}