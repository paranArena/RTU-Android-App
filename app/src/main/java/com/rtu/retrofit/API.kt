package com.rtu.retrofit

import com.rtu.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface API {
    @POST("/authenticate") // Call<InitializeResponse> 데이터를 받을 data class
    fun loginPostRequest(
        @Body initializeRequest: LoginRequest
    ): Call<LoginResponse> // InitializeRequest 요청을 보낼 Json Data Class

    @POST("/signup")
    fun registerPostRequest(
        @Body initializeRequest: RegisterRequest
    ): Call<RegisterResponse>

    @Multipart
    @POST("/clubs")
    fun createClubRequest(
        @Part ("name") name: RequestBody,
        @Part ("introduction") introduction: RequestBody,
        @Part thumbnail: MultipartBody.Part?,
        @Part ("hashtags")  hashtag: RequestBody
    ): Call<CreateClubResponse>

    @Multipart
    @POST("/clubs/{id}/notifications")
    fun createNoticeRequest(
        @Part ("title") name: RequestBody,
        @Part ("content") introduction: RequestBody,
        @Part image: MultipartBody.Part?,
        @Path("id") id:Int
    ): Call<CreateNoticeResponse>

    @GET("/members/{email}/exists")
    fun checkEmailRequest(
        @Path("email") email:String
    ): Call<Boolean>

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

    @GET("/clubs/{id}/notifications/search/all")
    fun getClubNotice(
        @Path("id") id: Int
    ): Call<MyNotice>

    @GET("/clubs/{club_id}/notifications/{notice_id}")
    fun getNoticeDetail(
        @Path("club_id") club_id: Int,
        @Path("notice_id") notice_id: Int
    ): Call<NoticeInfoModel>

    @GET("/members/my/notifications")
    fun getMyClubNotice(
    ): Call<MyNotice>

    @GET("/members/my/clubs/{id}/role")
    fun getMyClubRole(
        @Path("id") id: Int
    ): Call<MyRole>

    @POST("/clubs/{id}/requests/join")
    fun getJoinClub(
        @Path("id") id: Int
    ): Call<JoinResponse>

}