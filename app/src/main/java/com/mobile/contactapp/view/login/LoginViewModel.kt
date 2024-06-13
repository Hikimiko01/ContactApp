package com.mobile.contactapp.view.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.contactapp.data.UserRepository
import com.mobile.contactapp.data.api.response.LoginResponse
import com.mobile.contactapp.data.pref.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    fun login(email: String, password: String, callback: (LoginResponse) -> Unit) {
        viewModelScope.launch {
            try {
                val response = repository.login(email, password)
                withContext(Dispatchers.Main) {
                    callback(response)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    callback(
                        LoginResponse(
                            error = true,
                            message = e.localizedMessage
                        )
                    )
                }
            }
        }
    }
}