package com.mobile.contactapp.data.pref

data class Contact (
    val kontak: ContactItem
)

data class ContactItem (
    val firstName: String,
    val lastName: String? = "",
    val email: String,
    val phoneNumber: Long
)