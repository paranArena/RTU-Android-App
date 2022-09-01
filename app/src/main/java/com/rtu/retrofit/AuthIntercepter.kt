package com.rtu.retrofit

import android.content.Context
import android.content.SharedPreferences
import com.rtu.MainActivity
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override  fun intercept(chain: Interceptor.Chain): Response {
        val request=chain.request().newBuilder()
            .addHeader("Authorization", "")
            .build()

        return chain.proceed(request)
    }

    private fun getToken(): String {
        return MainActivity.GlobalApplication.prefs.getString("tokens", "xxxxxx")
    }
}

class PreferenceUtil(context: Context)
{
    private val prefs: SharedPreferences =
        context.getSharedPreferences("tokens", Context.MODE_PRIVATE)

    fun getString(key: String, defValue: String): String
    {
        return prefs.getString(key, defValue).toString()
    }

    fun setString(key: String, str: String)
    {
        prefs.edit().putString(key, str).apply()
    }
}