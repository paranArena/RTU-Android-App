package com.rtu.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

data class GroupModel(
    val name: String,
    val introduction: String
)

data class GetGroupModel(
    val statusCode: Int,
    val responseMessage: String,
    val data: List<ClubInfo>
)

@Parcelize
data class ClubInfo(
    val id: Int,
    val club: ClubSearchDetail,
    val role: String
): Parcelable


data class GetSearchGroup(
    val statusCode: Int,
    val responseMessage: String,
    val data: List<ClubSearchDetail>
)

@Parcelize
data class ClubSearchDetail(
    val id: Int,
    val name: String,
    val introduction: String,
    val thumbnailPath: String,
    val hashtags: ArrayList<String>
): Parcelable




data class ClubDetail(
    val statusCode: Int,
    val responseMessage: String,
    val data: ClubData
    )

@Parcelize
data class ClubData(
    val id: Int,
    val name: String,
    val introduction: String,
    val thumbnailPath: String,
    val hashtags: ArrayList<String>,
    val memberList: List<MemberInfo>,
    val notifications: List<NoticeDetailModel>,
    val products: ArrayList<String>
): Parcelable
