package com.ren2u.model

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

data class ImageResponse(
    val statusCode: Int,
    val responseMessage: String,
    val data: ImageModel
)

data class ImageModel(
    val url: String
)