package com.aplikasi.capstone.register
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aplikasi.capstone.api.ApiConfig
import com.aplikasi.capstone.UserPreferences
import com.aplikasi.capstone.response.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegisterViewModel(private val pref: UserPreferences): ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _registResponse = MutableLiveData<RegisterResponse>()
    val registResponse: LiveData<RegisterResponse> = _registResponse

    private val _errorCode = MutableLiveData<Int>()
    val errorCode: LiveData<Int> = _errorCode

    fun registAccount(name: String, email: String, password: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().registerAccount(name, email, password)
        client.enqueue(object: Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                _isLoading.value = false
                if (response.isSuccessful){
                    _registResponse.value = response.body()
                } else {
                    _errorCode.value = response.code()
                    Log.e(TAG, "onFailure: ${response.message()}")
                    Log.e(TAG, "onFailure: ${response.code()}")
                    Log.e(TAG, "onFailure: ${response.errorBody()}")
                    _errorCode.value = -1
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _isLoading.value = true
                Log.e(TAG, "onFailure: ${t.message}", )
            }
        })
    }
    companion object {
        private const val TAG = "RegistViewModel"
    }
}