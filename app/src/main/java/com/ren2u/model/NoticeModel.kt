package com.ren2u.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class MyNotice(
    val statusCode: Int,
    val responseMessage: String,
    val data: List<NoticeModel>
)

data class CreateNoticeResponse(
    val statusCode: Int,
    val responseMessage: String,
    val data: NoticeResponse
)

data class CreateNoticeRequest(
    val title: String,
    val content: String,
    val imagePaths: List<String>,
    val isPublic: Boolean
)

@Parcelize
data class NoticeResponse(
    val id: Int,
    val title: String,
    val content: String,
    val imagePath: String,
    val createdAt: String,
    val updatedAt: String
): Parcelable

@Parcelize
data class NoticeModel(
    val id: Int,
    val clubId: Int?,
    val title: String,
    val imagePath: String,
    val clubName: String,
    val isPublic: Boolean,
    val createdAt: String,
    val updatedAt: String
): Parcelable

data class NoticeInfoModel(
    val statusCode: Int,
    val responseMessage: String,
    val data: NoticeDetailModel
)

@Parcelize
data class NoticeDetailModel(
    val id: Int,
    val title: String,
    val content: String,
    val imagePath: String,
    val isPublic: Boolean,
    val createdAt: String,
    val updatedAt: String
): Parcelable
