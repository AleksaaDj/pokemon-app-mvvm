package com.aleksa.samaritanassignment.fragments.captured

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aleksa.samaritanassignment.models.CapturedItem
import com.aleksa.samaritanassignment.network.MainRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class CapturedViewModel constructor(private val repository: MainRepository) : ViewModel() {

    val captured = MutableLiveData<List<CapturedItem>>()

    fun getCaptured(token: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = repository.getCaptured("Bearer $token")
            withContext(Dispatchers.Main) {
                try {
                    if (response.isSuccessful) {
                        captured.postValue(response.body())
                    } else {
                        Log.e("GetCaptured", response.code().toString())
                    }
                } catch (e: HttpException) {
                    Log.e("GetCaptured", e.message())
                } catch (e: Throwable) {
                    Log.e("GetCaptured", "Something went wrong")
                }
            }
        }
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