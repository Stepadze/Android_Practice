package com.example.practice3final.data.network

import com.example.practice3final.data.api.OmdbApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {
    private val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .connectTimeout(120, TimeUnit.SECONDS)   // было 60, стало 120
        .readTimeout(120, TimeUnit.SECONDS)      // было 60, стало 120
        .writeTimeout(120, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.omdbapi.com/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: OmdbApi = retrofit.create(OmdbApi::class.java)
}