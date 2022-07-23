package com.rtu.repository

import android.app.Application
import com.rtu.component.LoginComponent
import com.rtu.retrofit2.API
import com.rtu.retrofit2.RetrofitBuilder


class MainRepository() {
    suspend fun loginPostRequest(request: LoginComponent)=
        RetrofitBuilder.api.loginPostRequest(request)
}