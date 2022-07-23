package com.rtu.viewmodel

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.rtu.R
import com.rtu.component.LoginComponent
import com.rtu.component.LoginResponse
import com.rtu.repository.MainRepository
import com.rtu.repository.SingleLiveEvent
import kotlinx.coroutines.launch

class MainViewModel(private val repository: MainRepository): ViewModel(){
    private val _loginResponse=MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginResponse

    fun postLoginRequest(postLoginRequest: LoginComponent){
        viewModelScope.launch {
            val response=repository.loginPostRequest(postLoginRequest)

            if(response.isSuccessful){
                _loginResponse.postValue(response.body())
            }
            else{
                Log.d("failed",response.body().toString())
                return@launch
            }
        }
    }

}