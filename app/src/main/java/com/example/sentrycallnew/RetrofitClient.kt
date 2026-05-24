package com.example.sentrycallnew

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // 🔥 TEMP URL (we will replace after AWS setup)
    private const val BASE_URL = "https://ibwy7pz681.execute-api.us-east-1.amazonaws.com/prod/"

    val api: ApiService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)
}