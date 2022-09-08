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
        @Part rentalPolices: List<MultipartBody.Part>
        //@Part ("hashtags")  hashtag: RequestBody
    ): Call<CreateClubResponse>

    @Multipart
    @POST("/clubs/{id}/notifications")
    fun createNoticeRequest(
        @Part ("title") name: RequestBody,
        @Part ("content") introduction: RequestBody,
        @Part image: MultipartBody.Part?,
        @Path("id") id:Int
    ): Call<CreateNoticeResponse>

    @Multipart
    @POST("/clubs/{id}/products")
    fun createProductRequest(
        @Part ("name") name: RequestBody,
        @Part ("category") category: RequestBody,
        @Part ("price") price: RequestBody,
        //@Part ("rentalPolicies") rentalPolicies: RequestBody,
        @Part rentalPolicies: List<MultipartBody.Part>,
        @Part ("fifoRentalPeriod") fifoRentalPeriod: RequestBody,
        @Part ("reserveRentalPeriod") reserveRentalPeriod: RequestBody,
        @Part ("locationName") locationName: RequestBody,
        @Part ("latitude") latitude: RequestBody,
        @Part ("longitude") longitude: RequestBody,
        @Part ("caution") caution: RequestBody,
        @Part image: MultipartBody.Part?,
        @Path("id") id:Int
    ): Call<CreateProductResponse>

    @GET("/members/{email}/exists")
    fun checkEmailRequest(
        @Path("email") email:String
    ): Call<Boolean>

    @GET("/clubs/search/all")
    fun groupSearchRequest(
    ): Call<GetSearchGroup>

    @GET("/clubs/search?name={word}")
    fun groupSearchNameRequest(
        @Path("word") word:String
    ): Call<SearchNameModel>

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

    @POST("/clubs/{id}/requests/join") //클럽가입
    fun getJoinClub(
        @Path("id") id: Int
    ): Call<JoinResponse>

    @DELETE("/clubs/{id}/requests/join/cancel") //가입 신청 취소
    fun getJoinClubCancel(
        @Path("id") id: Int
    ): Call<JoinResponse>

    @DELETE("/clubs/{id}/requests/join/cancel") //가입 신청 취소
    fun getLeaveClub(
        @Path("id") id: Int
    ): Call<JoinResponse>

    @GET("/clubs/{id}/members/search/all")
    fun getAllMember(
        @Path("id") id: Int
    ): Call<MemberListModel>

    @GET("/clubs/{id}/requests/join/search/all")
    fun getJoinRequest(
        @Path("id") id: Int
    ): Call<MemberListModel>

    @POST("/clubs/{club_id}/requests/join/{member_id}")
    fun getAcceptRequest(
        @Path("club_id") clubId: Int,
        @Path("member_id") studentId: Int
    ): Call<ResponseModel>

    @POST("/members/email/requestCode")
    fun getRequestCode(
        @Body requestMail: MailModel
    ): Call<BasicResponse>

    @POST("/members/email/verifyCode")
    fun getVerifyCode(
        @Body requestMail: MailCodeModel
    ): Call<BasicResponse>

    @GET("/members/my/products")
    fun getMyProducts(
    ): Call<GetProductModel>

    @GET("/clubs/{club_id}/products/{product_id}")
    fun getProduct(
        @Path("club_id") clubId: Int,
        @Path("product_id") productId: Int
    ): Call<GetProductResponse>

    @POST("/clubs/{club_id}/rentals/{item_id}/request")
    fun requestRent(
        @Path("club_id") clubId: Int,
        @Path("item_id") itemId: Int
    ): Call<RequestRentResponse>

    @PUT("/clubs/{club_id}/rentals/{item_id}/apply")
    fun applyRent(
        @Path("club_id") clubId: Int,
        @Path("item_id") itemId: Int
    ): Call<RentResponse>

    @PUT("/clubs/{club_id}/rentals/{item_id}/return")
    fun returnRent(
        @Path("club_id") clubId: Int,
        @Path("item_id") itemId: Int
    ): Call<RentResponse>

    @GET("/members/my/rentals")
    fun getMyRentals(
    ): Call<MyRentalResponse>

    @GET("/clubs/{club_id}/rentals/search/all")
    fun searchClubRentalsAll(
        @Path("club_id") clubId: Int
    ): Call<ManageRentModel>

    @GET("/clubs/{club_id}/rentals/history/search/all")
    fun searchClubReturnAll(
        @Path("club_id") clubId: Int
    ): Call<ReturnModel>

    @GET("/clubs/{club_id}/products/search/all")
    fun searchClubProductsAll(
        @Path("club_id") clubId: Int
    ): Call<GetProductModel>
}