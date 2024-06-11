package com.mobile.contactapp.data.api.retrofit

import com.mobile.contactapp.data.api.response.AddContactResponse
import com.mobile.contactapp.data.api.response.DeleteContactResponse
import com.mobile.contactapp.data.api.response.GetContactsResponse
import com.mobile.contactapp.data.api.response.LoginResponse
import com.mobile.contactapp.data.api.response.LogoutResponse
import com.mobile.contactapp.data.api.response.PostContactResponse
import com.mobile.contactapp.data.api.response.PutContactResponse
import com.mobile.contactapp.data.api.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @FormUrlEncoded
    @POST("auth/register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @GET("auth/logout")
    suspend fun logout() : LogoutResponse

    @GET("contact")
    suspend fun getContacts() : GetContactsResponse

    @POST("contact")
    suspend fun postContact(
        @Field("firstName") firstName: String,
        @Field("lastName") lastName: String?,
        @Field("email") email: String,
        @Field("phoneNumber") phoneNumber: Int
    ) : PostContactResponse

    @PUT("contact/{id}")
    suspend fun putContacts(
        @Path("id") id: String,
        @Field("firstName") firstName: String,
        @Field("lastName") lastName: String?,
        @Field("email") email: String,
        @Field("phoneNumber") phoneNumber: Int
    ) : PutContactResponse

    @DELETE("contact/{id}")
    suspend fun deleteContact(
        @Path("id") id: String
    ) : DeleteContactResponse

}