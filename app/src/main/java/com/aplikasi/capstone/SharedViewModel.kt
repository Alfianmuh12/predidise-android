package com.aplikasi.capstone

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.*
import com.aplikasi.capstone.api.ApiConfig
import com.aplikasi.capstone.response.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SharedViewModel(private val pref: UserPreferences) :
    ViewModel() {
    private val _listTourism = MutableLiveData<List<ResultItem>>()
    val list: LiveData<List<ResultItem>> = _listTourism

    private val _user = MutableLiveData<List<ResultUser>>()
    val dataUser: LiveData<List<ResultUser>> = _user

    val listOfTourism = MutableLiveData<List<ResultItem>>()

    private val _listReview = MutableLiveData<List<Review>>()
    val _review: LiveData<List<Review>> = _listReview

    val listOfReview = MutableLiveData<List<Review>>()

    val userDetail = MutableLiveData<List<ResultUser>>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading




    fun getUser(): MutableLiveData<UserModel> {
        return pref.getUser().asLiveData() as MutableLiveData<UserModel>
    }
    fun getStory(token: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getAllTourism(token)
        client.enqueue(object : Callback<AllTourismResponse> {
            override fun onResponse(call: Call<AllTourismResponse>, response: Response<AllTourismResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listTourism.value = response.body()?.result
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    Log.e(TAG, "onFailure: ${response.code()}")
                    Log.e(TAG, "onFailure: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<AllTourismResponse>, t: Throwable) {
                _isLoading.value = true
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun getSearchUsers(): LiveData<List<ResultItem>> {
        return listOfTourism
    }
    fun setSearchUser(token: String, query: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getSeacrhTourism(token, query)
        client.enqueue(object : Callback<AllTourismResponse> {
            override fun onResponse(call: Call<AllTourismResponse>, response: Response<AllTourismResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    listOfTourism.value = response.body()?.result
                } else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                    Log.e(ContentValues.TAG, "onFailure: ${response.code()}")
                    Log.e(ContentValues.TAG, "onFailure: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<AllTourismResponse>, t: Throwable) {
                _isLoading.value = true
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun getReview(): LiveData<List<Review>> {
        return listOfReview
    }
    fun setReview(id: String, token: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getReview(id, token)
        client.enqueue(object : Callback<ReviewResponse> {
            override fun onResponse(call: Call<ReviewResponse>, response: Response<ReviewResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    listOfReview.value = response.body()?.result
                } else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                    Log.e(ContentValues.TAG, "onFailure: ${response.code()}")
                    Log.e(ContentValues.TAG, "onFailure: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<ReviewResponse>, t: Throwable) {
                _isLoading.value = true
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }
        })
    }
    fun setRecomendation(token: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getAllTourismRecomendation(token)
        client.enqueue(object : Callback<AllTourismResponse> {
            override fun onResponse(call: Call<AllTourismResponse>, response: Response<AllTourismResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    listOfTourism.value = response.body()?.result
                } else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                    Log.e(ContentValues.TAG, "onFailure: ${response.code()}")
                    Log.e(ContentValues.TAG, "onFailure: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<AllTourismResponse>, t: Throwable) {
                _isLoading.value = true
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun getDetailUser(): MutableLiveData<List<ResultUser>> {
        return userDetail
    }

    fun setUserDetail(token: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserTourism(token)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    userDetail.value = response.body()?.result
                } else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                    Log.e(ContentValues.TAG, "onFailure: ${response.code()}")
                    Log.e(ContentValues.TAG, "onFailure: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _isLoading.value = true
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun setNature(token: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getNatureTourism(token)
        client.enqueue(object : Callback<AllTourismResponse> {
            override fun onResponse(call: Call<AllTourismResponse>, response: Response<AllTourismResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    listOfTourism.value = response.body()?.result
                } else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                    Log.e(ContentValues.TAG, "onFailure: ${response.code()}")
                    Log.e(ContentValues.TAG, "onFailure: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<AllTourismResponse>, t: Throwable) {
                _isLoading.value = true
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun setHistory(token: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getHistory(token)
        client.enqueue(object : Callback<AllTourismResponse> {
            override fun onResponse(call: Call<AllTourismResponse>, response: Response<AllTourismResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    listOfTourism.value = response.body()?.result
                } else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                    Log.e(ContentValues.TAG, "onFailure: ${response.code()}")
                    Log.e(ContentValues.TAG, "onFailure: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<AllTourismResponse>, t: Throwable) {
                _isLoading.value = true
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }
        })
    }


    fun saveUser(user: UserModel) {
        viewModelScope.launch {
            pref.saveUser(user)
        }
    }

    fun logout() {
        viewModelScope.launch {
            pref.clearUser()

        }
    }

    //val pagingStory: (String) -> LiveData<PagingData<ResultItem>> = { token: String ->
    //    storyRepo.getTourism("Bearer $token").cachedIn(viewModelScope)
    //}
}