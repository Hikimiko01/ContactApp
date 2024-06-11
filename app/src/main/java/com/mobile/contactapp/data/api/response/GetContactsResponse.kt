package com.mobile.contactapp.data.api.response

import com.google.gson.annotations.SerializedName

data class GetContactsResponse(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("contacts")
	val contacts: List<ListContacts> = emptyList()
)

data class ListContacts(

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("firstName")
	val firstName: String,

	@field:SerializedName("lastName")
	val lastName: String? = "",

	@field:SerializedName("phoneNumber")
	val phoneNumber: Long,

	@field:SerializedName("email")
	val email: String? = ""
)
