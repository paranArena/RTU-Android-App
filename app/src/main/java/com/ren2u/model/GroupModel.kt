package com.ren2u.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class GroupModel(
    val name: String,
    val introduction: String
)

@Parcelize
data class CreateClubModel(
    val name: String,
    val introduction: String,
    val thumbnailPath: List<String>,
    val hashtags: List<String>
): Parcelable

@Parcelize
data class CreateClubModelV1(
    val name: String,
    val intro: String,
    val imagePaths: List<String>,
    val hashtags: List<String>
): Parcelable


data class SearchNameModel(
    val statusCode: Int,
    val responseMessage: String,
    val data: ClubSearchDetail
)

data class GetGroupModel(
    val statusCode: Int,
    val responseMessage: String,
    val data: List<ClubInfo>
)

@Parcelize
data class ClubInfo(
    val id: Int,
    val name: String,
    val introduction: String,
    val thumbnailPath: String,
    val hashtags: List<String>,
    val clubRole: String
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
    val hashtags: List<String>,
    val clubRole: String
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
    val hashtags: List<String>
): Parcelable


data class CreateClubResponse(
    val statusCode: Int,
    val responseMessage: String,
    val data: ClubSearchDetail
)

data class JoinResponse(
    val statusCode: Int,
    val responseMessage: String,
    val data: String?
)