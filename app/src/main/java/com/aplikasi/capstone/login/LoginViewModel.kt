package com.aplikasi.capstone.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aplikasi.capstone.UserModel
import com.aplikasi.capstone.UserPreferences
import com.aplikasi.capstone.api.ApiConfig
import com.aplikasi.capstone.response.LoginResponse
import com.aplikasi.capstone.response.LoginResult
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginViewModel(private val pref: UserPreferences) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginResponse

    private val _errorCode = MutableLiveData<Int>()
    val errorCode: LiveData<Int> = _errorCode

    fun loginAccount(email: String, password: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().loginAccount(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _loginResponse.value = response.body()
                    _loginResult.value = response.body()?.loginResult
                } else {
                    _errorCode.value = response.code()
                    Log.e(TAG, "onFailure: ${response.message()}")
                    Log.e(TAG, "onFailure: ${response.code()}")
                    Log.e(TAG, "onFailure: ${response.errorBody().toString()}")
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    Log.e(TAG, "NJAY: ${jObjError.toString()}")

                    _errorCode.value = -1
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = true
                Log.e(TAG, "onFailure: ${t.message}")
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })

    }
    fun saveAccount(account: UserModel) {
        viewModelScope.launch {
            pref.saveUser(account)
        }
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}