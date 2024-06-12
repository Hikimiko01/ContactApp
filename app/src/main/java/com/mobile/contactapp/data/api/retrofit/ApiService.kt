package com.mobile.contactapp.data.api.retrofit

import com.mobile.contactapp.data.api.response.AddContactResponse
import com.mobile.contactapp.data.api.response.DeleteContactResponse
import com.mobile.contactapp.data.api.response.GetContactsResponse
import com.mobile.contactapp.data.api.response.LoginResponse
import com.mobile.contactapp.data.api.response.EditContactResponse
import com.mobile.contactapp.data.api.response.RegisterResponse
import com.mobile.contactapp.data.pref.Contact
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @FormUrlEncoded
    @POST("auth/register")
    suspend fun register(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("contact")
    suspend fun getContacts() : GetContactsResponse

    @POST("contact")
    suspend fun addContact(
        @Body kontak: Contact
    ) : AddContactResponse

    @PUT("contact/{id}")
    suspend fun putContacts(
        @Path("id") id: String,
        @Field("firstName") firstName: String,
        @Field("lastName") lastName: String?,
        @Field("email") email: String,
        @Field("phoneNumber") phoneNumber: Int
    ) : EditContactResponse

    @DELETE("contact/{id}")
    suspend fun deleteContact(
        @Path("id") id: String
    ) : DeleteContactResponse

}