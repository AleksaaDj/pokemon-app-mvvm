package com.aleksa.samaritanassignment.fragments.myteam

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aleksa.samaritanassignment.models.Captured
import com.aleksa.samaritanassignment.models.MyTeam
import com.aleksa.samaritanassignment.models.MyTeamItem
import com.aleksa.samaritanassignment.network.MainRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyTeamViewModel constructor(private val repository: MainRepository) : ViewModel() {

    val myTeam = MutableLiveData<List<MyTeamItem>>()

    fun getMyTeam(token: String) {
        val response = repository.getMyTeam("Bearer $token")
        response.enqueue(object : Callback<List<MyTeamItem>> {
            override fun onResponse(call: Call<List<MyTeamItem>>, response: Response<List<MyTeamItem>>) {
                val responseBody = response.body()
                myTeam.postValue(responseBody)
            }
            override fun onFailure(call: Call<List<MyTeamItem>>, t: Throwable) {
                Log.e("GetMyTeam", t.message.toString())
            }
        })
    }

    class ViewModelFactory constructor(private val repository: MainRepository) :
        ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(MyTeamViewModel::class.java)) {
                MyTeamViewModel(this.repository) as T
            } else {
                throw IllegalArgumentException("ViewModel Not Found")
            }
        }
    }
}