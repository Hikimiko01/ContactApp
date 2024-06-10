package com.mobile.contactapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.mobile.contactapp.data.api.response.ListContacts
import com.mobile.contactapp.data.pref.UserModel
import kotlinx.coroutines.launch

class MainViewModel(private val repository: com.mobile.contactapp.data.UserRepository) : ViewModel() {

    private val _contacts = MutableLiveData<List<ListContacts>>()
    val contacts: LiveData<List<ListContacts>> = _contacts

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun getContacts(token: String){
        viewModelScope.launch {
            try {
                val stories = repository.getContacts(token)
                _contacts.value = stories ?: emptyList()
            } catch (e: Exception) {
                _contacts.value = emptyList()
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

}