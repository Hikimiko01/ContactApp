package com.mobile.contactapp.view.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.mobile.contactapp.data.UserRepository
import com.mobile.contactapp.data.api.response.ListContacts
import com.mobile.contactapp.data.pref.Contact
import com.mobile.contactapp.data.pref.UserModel
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository) : ViewModel() {

    private val _contacts = MutableLiveData<List<ListContacts>>()
    val contacts: LiveData<List<ListContacts>> = _contacts

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun addContact(kontak: Contact){
        viewModelScope.launch {
            try {
                repository.addContact(kontak)
                val updatedContacts = repository.getContacts(repository.getSession().asLiveData().value?.token ?: "")
                _contacts.value = updatedContacts
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getContacts(token: String){
        viewModelScope.launch {
            try {
                val contacts = repository.getContacts(token)
                Log.d("MainViewModel", "Fetched contacts: $contacts")
                _contacts.value = contacts
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error fetching contacts", e)
                _contacts.value = emptyList()
            }
        }
    }

    fun deleteContact(id: String) {
        viewModelScope.launch {
            try {
                Log.d("MainViewModel", "Deleting contact: $id")
                repository.deleteContact(id)
                val updatedContacts = repository.getContacts(repository.getSession().asLiveData().value?.token ?: "")
                _contacts.value = updatedContacts
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

}