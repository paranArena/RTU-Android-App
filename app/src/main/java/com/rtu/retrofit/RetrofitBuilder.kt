package com.rtu.retrofit

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {
    var api: API
    private const val BASE_URL="http://ec2-3-35-241-206.ap-northeast-2.compute.amazonaws.com:8080"

    init {
        val gson= GsonBuilder()
            .setLenient()
            .create()
        val client= OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        api = retrofit.create(API::class.java)
    }
}