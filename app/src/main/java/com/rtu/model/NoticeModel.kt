package com.rtu.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class MyNotice(
    val statusCode: Int,
    val responseMessage: String,
    val data: List<NoticeModel>
)

@Parcelize
data class NoticeModel(
    val id: Int,
    val title: String,
    val createdAt: String,
    val updatedAt: String
): Parcelable

@Parcelize
data class NoticeDetailModel(
    val id: Int,
    val title: String,
    val content: String,
    val createdAt: String,
    val updatedAt: String
): Parcelable
