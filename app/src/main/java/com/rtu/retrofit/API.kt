package com.rtu.retrofit

import com.rtu.model.LoginRequest
import com.rtu.model.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface API {
    @POST("/authenticate") // Call<InitializeResponse> 데이터를 받을 data class
    fun loginPostRequest(
        @Body initializeRequest: LoginRequest
    ): Call<LoginResponse> // InitializeRequest 요청을 보낼 Json Data Class

}