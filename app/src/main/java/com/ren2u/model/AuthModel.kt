package com.ren2u.model
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val token: String?
)

@Parcelize
data class Authority(
    val authorityName: String
): Parcelable

data class RegisterRequest(
    val email: String,
    val password: String,
    val name: String,
    val phoneNumber: String,
    val studentId: String,
    val major: String,
    val verificationCode: String
)

data class RegisterResponse(
    val statusCode: Int,
    val responseMessage: String,
    val data: RegisterData
)

@Parcelize
data class RegisterData(
    val id: Int,
    val email: String,
    val name: String,
    val phoneNumber: String,
    val studentId: String,
    val major: String,
    val activated: Boolean,
    val clubList: List<Club>?,
    val rentals: List<Rental>?,
    val authorityDtoSet: List<Authority>
): Parcelable

data class MailModel(
    val email: String
)

data class MailCodeModel(
    val email: String,
    val code: String
)

data class BasicResponse(
    val statusCode: Int,
    val responseMessage: String,
    val data: String?
)

data class VerifyModel(
    val email: String,
    val code: String,
    val password: String
)

@Parcelize
data class Club(
    val item: String
): Parcelable

@Parcelize
data class Rental(
    val item: String
): Parcelable

data class FcmModel(
    val memberId: Int,
    val fcmToken: String
)
