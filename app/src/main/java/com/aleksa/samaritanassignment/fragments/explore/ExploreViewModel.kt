package com.aleksa.samaritanassignment.fragments.explore

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aleksa.samaritanassignment.models.PokemonList
import com.aleksa.samaritanassignment.models.Token
import com.aleksa.samaritanassignment.network.MainRepository
import kotlinx.coroutines.*
import retrofit2.HttpException


class ExploreViewModel(private val repository: MainRepository) : ViewModel() {

    val pokemonListLiveData = MutableLiveData<PokemonList>()
    val tokenLiveData = MutableLiveData<Token>()

    fun getToken(){
        CoroutineScope(Dispatchers.IO).launch {
            val response = repository.getToken()
            withContext(Dispatchers.Main) {
                try {
                    if (response.isSuccessful) {
                        tokenLiveData.postValue(response.body())
                    } else {
                        Log.e(TAG_TOKEN, response.code().toString())
                    }
                } catch (e: HttpException) {
                    Log.e(TAG_TOKEN, e.message())
                } catch (e: Throwable) {
                    Log.e(TAG_TOKEN, "Something went wrong")
                }
            }
        }
    }
    fun getPokemonList(limit: String){
        CoroutineScope(Dispatchers.IO).launch {
            val response = repository.getPokemonList(limit)
            withContext(Dispatchers.Main) {
                try {
                    if (response.isSuccessful) {
                        pokemonListLiveData.postValue(response.body())
                    } else {
                        Log.e(TAG_POKEMON_LIST, response.code().toString())
                    }
                } catch (e: HttpException) {
                    Log.e(TAG_POKEMON_LIST, e.message())
                } catch (e: Throwable) {
                    Log.e(TAG_POKEMON_LIST, e.message.toString())
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class ViewModelFactory(private val repository: MainRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(ExploreViewModel::class.java)) {
                ExploreViewModel(this.repository) as T
            } else {
                throw IllegalArgumentException("ViewModel Not Found")
            }
        }
    }
    companion object{
        const val TAG_POKEMON_LIST = "GetPokemonList"
        const val TAG_TOKEN = "GetToken"
    }
}