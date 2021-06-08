package ua.yandex.jere184.c4tappydefender.ui.fragments.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.yandex.jere184.c4tappydefender.R
import ua.yandex.jere184.c4tappydefender.model.User
import ua.yandex.jere184.c4tappydefender.repository.IUserRepository
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val userRepository: IUserRepository
) : ViewModel() {

    var currentPayerShipIndex = 0
        private set

    private fun incrementIconPosition() {
        currentPayerShipIndex++
    }

    private val userDataLiveData by lazy {
        MutableLiveData<User>()
    }

    fun getUserDataLiveData(): LiveData<User> = userDataLiveData

    fun saveUserData(key: String, user: User) {
        userRepository.save(key, user)
    }

    fun readUserData(key: String) {
        viewModelScope.launch(context = Dispatchers.IO) {
            val userData = userRepository.readAsync(key)
            withContext(Dispatchers.Main) {
                userDataLiveData.value = userData.await()
            }
        }
    }

    fun nextShipIcon(): Int {
        // смена изображения при нажатии на правую кнопку кнопку
        incrementIconPosition()
        if (currentPayerShipIndex > 2) {
            currentPayerShipIndex = 0
        }

        return getIconResId(currentPayerShipIndex)
    }

    private fun getIconResId(payerShipIndex: Int) = when (payerShipIndex) {
        0 -> {
            R.drawable.spaceship_1
        }
        1 -> {
            R.drawable.spaceship
        }
        else -> {
            R.drawable.spaceship_2
        }
    }
}