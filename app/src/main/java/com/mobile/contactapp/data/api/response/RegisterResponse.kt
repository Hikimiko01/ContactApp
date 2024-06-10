package com.mobile.contactapp.data.api.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse (

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("token")
    val token: String? = null

)