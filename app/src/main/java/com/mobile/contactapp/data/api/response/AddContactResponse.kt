package com.mobile.contactapp.data.api.response

import com.google.gson.annotations.SerializedName

data class AddContactResponse (
    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null,
)