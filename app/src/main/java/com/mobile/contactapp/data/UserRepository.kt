package com.mobile.contactapp.data

import com.mobile.contactapp.data.api.retrofit.ApiService
import com.mobile.contactapp.data.pref.UserModel
import com.mobile.contactapp.data.pref.UserPreference
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference

) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    suspend fun getContacts(token: String): List<com.mobile.contactapp.data.api.response.ListContacts>? {
        val response = apiService.getContacts()
        return if (!response.error!!) response.contacts else null
    }

    suspend fun login(email: String, password: String): com.mobile.contactapp.data.api.response.LoginResponse {
        return apiService.login(email, password)
    }

    suspend fun register(name: String, email: String, password: String): com.mobile.contactapp.data.api.response.RegisterResponse {
        return apiService.register(name, email, password)
    }

    suspend fun addStory(file: MultipartBody.Part, description: RequestBody): com.mobile.contactapp.data.api.response.AddContactResponse {
        return apiService.addStory(file, description)
    }

    companion object {
        @Volatile
        private var instance: com.mobile.contactapp.data.UserRepository? = null

        fun getInstance(apiService: ApiService, userPreference: UserPreference) =
            com.mobile.contactapp.data.UserRepository(apiService, userPreference)

    }
}