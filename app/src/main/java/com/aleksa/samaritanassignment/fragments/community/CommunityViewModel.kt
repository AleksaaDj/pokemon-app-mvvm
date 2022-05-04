package com.aleksa.samaritanassignment.fragments.community

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aleksa.samaritanassignment.models.*
import com.aleksa.samaritanassignment.network.MainRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class CommunityViewModel constructor(private val repository: MainRepository) : ViewModel() {

    val community = MutableLiveData<Community>()
    val loading = MutableLiveData<Boolean>()

    fun getCommunity(token: String) {
        loading.postValue(true)
        CoroutineScope(Dispatchers.IO).launch {
            val response = repository.getCommunity("Bearer $token")
            withContext(Dispatchers.Main) {
                try {
                    if (response.isSuccessful) {
                        community.postValue(response.body())
                    } else {
                        Log.e(TAG, response.code().toString())
                    }
                } catch (e: HttpException) {
                    Log.e(TAG, e.message())
                } catch (e: Throwable) {
                    Log.e(TAG, "Something went wrong")
                }
                loading.postValue(false)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
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

    companion object {
        const val TAG = "GetCommunity"
    }
}