package com.dicoding.storyapp.story

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.storyapp.UserModel
import com.dicoding.storyapp.UserPreference
import com.dicoding.storyapp.data.response.GetStoryResponse
import com.dicoding.storyapp.data.response.ListStoryItem
import com.dicoding.storyapp.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryViewModel(context: Context) : ViewModel() {
    private var mUserPreference: UserPreference = UserPreference(context)
    private var user = UserModel()

    private val _listStory = MutableLiveData<List<ListStoryItem>>()
    var listStory: LiveData<List<ListStoryItem>> = _listStory

    private val _isLoading = MutableLiveData<Boolean>()
    var isLoading: LiveData<Boolean> = _isLoading

    init {
        getAllStory()
    }

    fun refreshStory() {
        getAllStory()
    }

    private fun getAllStory() {
        _isLoading.value = true
        user = mUserPreference.getUser()
        val client = ApiConfig.getStoryApiService(user.token ?: "").getStory()
        client.enqueue(object : Callback<GetStoryResponse> {
            override fun onResponse(
                call: Call<GetStoryResponse>,
                response: Response<GetStoryResponse>
            ) {
                _isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    Log.d(TAG, responseBody.listStory.toString())
//                    setStoryData(responseBody.listStory)
                    _listStory.value = responseBody.listStory
                }
                else {
                    Log.e(TAG, "Unauthorized: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GetStoryResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }


    companion object {
        private const val TAG = "StoryViewModel"
    }
}