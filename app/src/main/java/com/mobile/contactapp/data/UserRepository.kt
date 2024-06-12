package com.mobile.contactapp.data

import com.mobile.contactapp.data.api.response.AddContactResponse
import com.mobile.contactapp.data.api.response.DeleteContactResponse
import com.mobile.contactapp.data.api.response.ListContacts
import com.mobile.contactapp.data.api.response.LoginResponse
import com.mobile.contactapp.data.api.response.EditContactResponse
import com.mobile.contactapp.data.api.response.RegisterResponse
import com.mobile.contactapp.data.api.retrofit.ApiService
import com.mobile.contactapp.data.pref.Contact
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

    suspend fun addContact(
        kontak: Contact
    ): AddContactResponse {
        return apiService.addContact(kontak)
    }

    suspend fun editContact(
        id: String,
        kontak: Contact
    ): EditContactResponse  {
        return apiService.putContacts(id, kontak)
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