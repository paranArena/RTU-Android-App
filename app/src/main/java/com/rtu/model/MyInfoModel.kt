package com.rtu.model


import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class MyInfoModel(
    val `data`: Data,
    val responseMessage: String,
    val statusCode: Int
)

data class Data(
    val activated: Boolean,
    val authorities: List<Authority>,
    val clubList: List<ClubList>,
    val email: String,
    val id: Int,
    val major: String,
    val name: String,
    val phoneNumber: String,
    val rentals: List<Any>,
    val studentId: String
)

@Parcelize
data class ClubList(
    val club: String?,
    val id: Int
): Parcelable

data class MemberInfoModel(
    val statusCode: Int,
    val responseMessage: String,
    val data: MemberInfo
)

@Parcelize
data class MemberInfo(
    val id: Int,
    val email: String,
    val name: String,
    val phoneNumber: String,
    val studentId: String,
    val major: String,
    val activated: Boolean,
    val authorities: List<Authority>
): Parcelable

data class MyRole(
    val statusCode: Int,
    val responseMessage: String,
    val data: ClubRole
)

@Parcelize
data class ClubRole(
    val clubRole: String
): Parcelable