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
    val loading = MutableLiveData<Boolean>()

    fun getMyTeam(token: String) {
        loading.postValue(true)
        CoroutineScope(Dispatchers.IO).launch {
            val myTeamItemRoomList = repository.getMyItemTable()
            if (myTeamItemRoomList.isEmpty()) {
                val response = repository.getMyTeam("Bearer $token")
                withContext(Dispatchers.Main) {
                    try {
                        if (response.isSuccessful) {
                            myTeam.postValue(response.body())
                            response.body()?.let { addMyTeamItemsToDatabase(it) }
                        } else {
                            Log.e(TAG, response.code().toString())
                        }
                    } catch (e: HttpException) {
                        Log.e(TAG, e.message())
                    } catch (e: Throwable) {
                        Log.e(TAG, "Something went wrong")
                    }
                }
            } else {
                myTeam.postValue(myTeamItemRoomList)
            }
            loading.postValue(false)
        }
    }

    private suspend fun addMyTeamItemsToDatabase(listMyTeamItem: List<MyTeamItem>){
        listMyTeamItem.let {
            for (myTeamItem in it) {
                repository.insertMyTeamItem(myTeamItem)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
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

    companion object {
        private const val  TAG = "GetMyTeam"
    }
}