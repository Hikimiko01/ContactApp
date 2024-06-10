package com.mobile.contactapp.di

import android.content.Context
import com.mobile.contactapp.data.pref.UserPreference
import com.mobile.contactapp.data.pref.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): com.mobile.contactapp.data.UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first()}
        val apiService = com.mobile.contactapp.data.api.retrofit.ApiConfig.getApiService(user.token)
        return com.mobile.contactapp.data.UserRepository.getInstance(apiService, pref)
    }
}