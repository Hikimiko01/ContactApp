package com.mobile.contactapp.data

import com.mobile.contactapp.data.api.response.DeleteContactResponse
import com.mobile.contactapp.data.api.response.ListContacts
import com.mobile.contactapp.data.api.response.LoginResponse
import com.mobile.contactapp.data.api.response.PostContactResponse
import com.mobile.contactapp.data.api.response.PutContactResponse
import com.mobile.contactapp.data.api.response.RegisterResponse
import com.mobile.contactapp.data.api.retrofit.ApiService
import com.mobile.contactapp.data.pref.UserModel
import com.mobile.contactapp.data.pref.UserPreference
import kotlinx.coroutines.flow.Flow

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

    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return apiService.register(name, email, password)
    }

    suspend fun login(username: String, password: String): LoginResponse {
        return apiService.login(username, password)
    }

    suspend fun logout(){
        userPreference.logout()
    }

    suspend fun getContacts(token: String): List<ListContacts> {
        return apiService.getContacts().contacts
    }

    suspend fun postContact(
        firstName: String,
        lastName: String,
        email: String,
        phoneNumber: Int
    ): PostContactResponse {
        return apiService.postContact(firstName, lastName, email, phoneNumber)
    }

    suspend fun putContact(
        id: String,
        firstName: String,
        lastName: String,
        email: String,
        phoneNumber: Int
    ): PutContactResponse  {
        return apiService.putContacts(id, firstName, lastName, email, phoneNumber)
    }

    suspend fun deleteContact(id: String): DeleteContactResponse {
        return apiService.deleteContact(id)
    }

    companion object {
        @Volatile
        private var instance: com.mobile.contactapp.data.UserRepository? = null

        fun getInstance(apiService: ApiService, userPreference: UserPreference) =
            com.mobile.contactapp.data.UserRepository(apiService, userPreference)

    }
}