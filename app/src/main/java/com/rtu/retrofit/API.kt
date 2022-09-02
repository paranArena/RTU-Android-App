package com.rtu.retrofit

import com.rtu.model.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface API {
    @POST("/authenticate") // Call<InitializeResponse> 데이터를 받을 data class
    fun loginPostRequest(
        @Body initializeRequest: LoginRequest
    ): Call<LoginResponse> // InitializeRequest 요청을 보낼 Json Data Class

    @POST("/signup")
    fun registerPostRequest(
        @Body initializeRequest: RegisterRequest
    ): Call<RegisterResponse>

    @GET("/clubs/search/all")
    fun groupSearchRequest(
    ): Call<GetSearchGroup>

    @GET("/members/my/clubs")
    fun myGroupRequest(
    ): Call<GetGroupModel>

    @GET("/members/my/info")
    fun myInfoRequest(
    ): Call<MyInfoModel>

    @GET("/clubs/{id}/info")
    fun getGroupDetail(
        @Path("id") id:Int
    ): Call<ClubDetail>

    //Todo 모든 공지사항 GET

}