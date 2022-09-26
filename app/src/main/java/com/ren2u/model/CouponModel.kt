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
    val data: ImageModel
)

data class ImageModel(
    val url: String
)