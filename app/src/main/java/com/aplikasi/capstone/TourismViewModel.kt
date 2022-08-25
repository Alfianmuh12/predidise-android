package com.aplikasi.capstone

import android.app.Application
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TourismViewModel(private val pref: UserPreferences, application: Application) : AndroidViewModel(application) {
    val detailUsers = MutableLiveData<ResultItem>()
    private val _listTourism = MutableLiveData<List<ResultItem>>()
    val list: LiveData<List<ResultItem>> = _listTourism

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

//    fun setDetail(token: String) {
//        val client = ApiConfig.getApiService().getAllTourism(token)
//        client.enqueue(object : Callback<AllTourismResponse> {
//            override fun onResponse(call: Call<AllTourismResponse>, response: Response<AllTourismResponse>) {
//                if (response.isSuccessful) {
//                    _listTourism.value = response.body()?.result
//                } else {
//                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
//                    Log.e(ContentValues.TAG, "onFailure: ${response.code()}")
//                    Log.e(ContentValues.TAG, "onFailure: ${response.errorBody()}")
//                }
//            }
//
//            override fun onFailure(call: Call<AllTourismResponse>, t: Throwable) {
//                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
//            }
//        })
//    }

    /*fun setDetailTourism(token: String, id: Int) {
        ApiClient.apiIns
            .getDetailTourism(token, id)
            .enqueue(object : Callback<ResultItem> {
                override fun onResponse(call: Call<ResultItem>, response: Response<ResultItem>) {
                    if (response.isSuccessful) {
                        detailUsers.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<ResultItem>, t: Throwable) {
                    t.message?.let { Log.d(FAIL, it) }
                }
            })

    }*/

    fun getDetailUser(): LiveData<ResultItem> {
        return detailUsers
    }

    companion object {
        const val FAIL = "Failure"
    }
}