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
    val loading = MutableLiveData<Boolean>()

    fun getCaptured(token: String) {
        loading.postValue(true)
        CoroutineScope(Dispatchers.IO).launch {
            val capturedItemRoomList = repository.getCapturedTable()
            if (capturedItemRoomList.isEmpty()) {
                val response = repository.getCaptured("Bearer $token")
                withContext(Dispatchers.Main) {
                    try {
                        if (response.isSuccessful) {
                            captured.postValue(response.body())
                            response.body()?.let { addCapturedItemsToDatabase(it) }
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
                captured.postValue(capturedItemRoomList)
            }
            loading.postValue(false)
        }
    }

    private suspend fun addCapturedItemsToDatabase(capturedItemList: List<CapturedItem>) {
        capturedItemList.let {
            for (capturedItem in it) {
                repository.insertCapturedItem(capturedItem)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
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
    companion object {
        const val TAG = "GetCaptured"
    }
}