package com.aleksa.samaritanassignment.fragments.myteam

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aleksa.samaritanassignment.models.MyTeamItem
import com.aleksa.samaritanassignment.network.MainRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class MyTeamViewModel constructor(private val repository: MainRepository) : ViewModel() {

    val myTeam = MutableLiveData<List<MyTeamItem>>()

    fun getMyTeam(token: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = repository.getMyTeam("Bearer $token")
            withContext(Dispatchers.Main) {
                try {
                    if (response.isSuccessful) {
                        myTeam.postValue(response.body())
                    } else {
                        Log.e("GetMyTeam", response.code().toString())
                    }
                } catch (e: HttpException) {
                    Log.e("GetMyTeam", e.message())
                } catch (e: Throwable) {
                    Log.e("GetMyTeam", "Something went wrong")
                }
            }
        }
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