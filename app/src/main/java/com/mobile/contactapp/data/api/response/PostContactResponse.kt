package com.mobile.contactapp.data.api.response

import com.google.gson.annotations.SerializedName

data class PostContactResponse (

    @field:SerializedName("message")
    val message: String
)