package com.aleksa.samaritanassignment.fragments.community

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aleksa.samaritanassignment.models.*
import com.aleksa.samaritanassignment.network.MainRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommunityViewModel constructor(private val repository: MainRepository) : ViewModel() {

    val community = MutableLiveData<Community>()

    fun getCommunity(token: String){
        val response = repository.getCommunity("Bearer $token")
        response.enqueue(object : Callback<Community> {
            override fun onResponse(call: Call<Community>, response: Response<Community>) {
                val responseBody = response.body()
                community.postValue(responseBody)
            }
            override fun onFailure(call: Call<Community>, t: Throwable) {
                Log.e("GetCommunity",t.message.toString())
            }
        })
    }

    class ViewModelFactory constructor(private val repository: MainRepository) :
        ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(CommunityViewModel::class.java)) {
                CommunityViewModel(this.repository) as T
            } else {
                throw IllegalArgumentException("ViewModel Not Found")
            }
        }
    }
}