package com.mobile.contactapp.data.api.retrofit

import com.mobile.contactapp.data.api.response.AddContactResponse
import com.mobile.contactapp.data.api.response.GetContactsResponse
import com.mobile.contactapp.data.api.response.LoginResponse
import com.mobile.contactapp.data.api.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @Multipart
    @POST("stories")
    suspend fun addStory(
        @Part photo: MultipartBody.Part,
        @Part("description") description: RequestBody
    ) : AddContactResponse

    @GET("stories")
    suspend fun getContacts() : GetContactsResponse

    // Still needed work to do

}