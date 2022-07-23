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