package com.aleksa.samaritanassignment.fragments.captured

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aleksa.samaritanassignment.models.Captured
import com.aleksa.samaritanassignment.models.CapturedItem
import com.aleksa.samaritanassignment.network.MainRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CapturedViewModel constructor(private val repository: MainRepository) : ViewModel() {

    val captured = MutableLiveData<List<CapturedItem>>()

    fun getCaptured(token: String) {
        val response = repository.getCaptured("Bearer $token")
        response.enqueue(object : Callback<List<CapturedItem>> {
            override fun onResponse(call: Call<List<CapturedItem>>, response: Response<List<CapturedItem>>) {
                val responseBody = response.body()
                captured.postValue(responseBody)
            }
            override fun onFailure(call: Call<List<CapturedItem>>, t: Throwable) {
                Log.e("GetCommunity", t.message.toString())
            }
        })
    }

    class ViewModelFactory constructor(private val repository: MainRepository) :
        ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(CapturedViewModel::class.java)) {
                CapturedViewModel(this.repository) as T
            } else {
                throw IllegalArgumentException("ViewModel Not Found")
            }
        }
    }
}