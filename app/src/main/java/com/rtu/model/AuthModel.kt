package com.rtu.model
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
    val major: String
)

data class RegisterResponse(
    val statusCode: Int,
    val responseMessage: String,
    val data: RegisterData
)

data class RegisterData(
    val id: Int,
    val email: String,
    val name: String,
    val phoneNumber: String,
    val studentId: String,
    val major: String,
    val activated: Boolean,
    val clubList: List<Club>,
    val rentals: List<Rental>,
    val authorityDtoSet: List<Authority>
)

@Parcelize
data class Club(
    val item: String
): Parcelable

@Parcelize
data class Rental(
    val item: String
): Parcelable