package ua.yandex.jere184.c4tappydefender.ui.fragments.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ua.yandex.jere184.c4tappydefender.model.User
import ua.yandex.jere184.c4tappydefender.repository.IUserRepository
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val userRepository: IUserRepository
) : ViewModel() {

    private val userDataLiveData by lazy {
        MutableLiveData<User>()
    }

    fun getUserDataLiveData(): LiveData<User> = userDataLiveData

    fun saveUserData(key: String, user: User) {
        userRepository.save(key, user)
    }
    fun readUserData(key: String) {
        viewModelScope.launch {
            val userData = userRepository.read(key)
            userDataLiveData.value = userData.await()
        }
    }
}