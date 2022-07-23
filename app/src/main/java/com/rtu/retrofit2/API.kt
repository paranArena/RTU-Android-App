package com.rtu.retrofit2

import com.rtu.component.LoginComponent
import com.rtu.component.LoginResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface API {
    @POST("authenticate") // login
    suspend fun loginPostRequest(
        @Body initializeRequest: LoginComponent
    ): Response<LoginResponse> // InitializeRequest 요청을 보낼 Json Data Class
}