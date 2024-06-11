package com.mobile.contactapp.data.api.response

import com.google.gson.annotations.SerializedName

data class PutContactResponse (

    @field:SerializedName("message")
    val message: String
)