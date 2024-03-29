package com.ren2u.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class CouponModel(
    val name: String,
    val locationName: String,
    val latitude: Double,
    val longitude: Double,
    val information: String,
    val imagePath: String,
    val actDate: String,
    val expDate: String
)

@Parcelize
data class CouponListModel(
    val id: Int,
    val clubId: Int,
    val clubName: String,
    val name: String,
    val imagePath: String,
    val actDate: String,
    val expDate: String
): Parcelable

data class CouponListResponse(
    val statusCode: Int,
    val responseMessage: String,
    val data: List<CouponListModel>
)

data class ImageResponse(
    val statusCode: Int,
    val responseMessage: String,
    val data: String
)

data class CouponInfoResponse(
    val statusCode: Int,
    val responseMessage: String,
    val data: CouponInfoModel
)

@Parcelize
data class CouponInfoModel(
    val id: Int,
    val name: String,
    val information: String,
    val imagePath: String,
    val location: LocationModel,
    val actDate: String,
    val expDate: String
): Parcelable

data class ImageModel(
    val url: String
)

data class AdminCouponResponse(
    val statusCode: Int,
    val responseMessage: String,
    val data: AdminCouponModel
)

@Parcelize
data class AdminCouponModel(
    val id: Int,
    val name: String,
    val information: String,
    val allCouponCount: Int,
    val leftCouponCount: Int,
    val imagePath: String,
    val location: LocationModel,
    val actDate: String,
    val expDate: String
): Parcelable

data class CouponMemberResponse(
    val statusCode: Int,
    val responseMessage: String,
    val data: List<CouponMemberModel>
)

@Parcelize
data class CouponMemberModel(
    val id: Int,
    val memberPreviewDto: MemberPreviewDto
): Parcelable

@Parcelize
data class MemberPreviewDto(
    val id: Int,
    val name: String,
    val major: String,
    val studentId: String
): Parcelable

data class CouponGrantRequest(
    val memberIds: List<Int>
)

data class UseCouponResponse(
    val code: String,
    val message: String
)