package com.rtu.model

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val statusCode: Int,
    val responseMessage: String,
    val data: DataList
)

data class DataList(
    val tokens: String
)

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
    val authorities: String?,
    val clubList: String?,
    val authorityDtoSet: String?
)