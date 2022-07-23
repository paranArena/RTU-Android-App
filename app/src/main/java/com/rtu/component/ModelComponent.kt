package com.rtu.component

data class LoginComponent(
    val email: String,
    val password: String,
)

data class LoginResponse(
    val statusCode: Int,
    val responseMessage: String,
    val data: DataList
)

data class DataList(
    val token: String
)
