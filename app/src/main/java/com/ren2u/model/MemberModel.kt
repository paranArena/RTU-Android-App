package com.ren2u.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

class MemberListModel(
    val statusCodes: Int,
    val responseMessage: String,
    val data: List<MemberModel>
)

@Parcelize
data class MemberModel(
    val id: Int,
    val email: String,
    val name: String,
    val phoneNumber: String,
    val studentId: String,
    val major: String,
    val clubRole: String
): Parcelable

class ResponseModel(
    val statusCodes: Int,
    val responseMessage: String,
    val data: String?
)