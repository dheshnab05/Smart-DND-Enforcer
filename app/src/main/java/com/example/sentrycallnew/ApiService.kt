package com.example.sentrycallnew

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Headers   // ✅ ADD THIS

interface ApiService {

    @Headers("x-api-key: my_secure_key_0987654321_secure")
    @POST("saveContact")
    fun sendContact(@Body contact: Contact): Call<okhttp3.ResponseBody>
}