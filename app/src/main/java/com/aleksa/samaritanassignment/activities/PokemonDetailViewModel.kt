package com.aleksa.samaritanassignment.activities

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aleksa.samaritanassignment.models.Pokemon
import com.aleksa.samaritanassignment.network.MainRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class PokemonDetailViewModel constructor(private val repository: MainRepository) : ViewModel() {

    val capturedPokemon = MutableLiveData<String>()
    val pokemonLiveData = MutableLiveData<Pokemon>()


    fun getPokemon(name: String){
        CoroutineScope(Dispatchers.IO).launch {
            val response = repository.getPokemon(name)
            withContext(Dispatchers.Main) {
                try {
                    if (response.isSuccessful) {
                        pokemonLiveData.postValue(response.body())
                    } else {
                        Log.e("GetPokemon", response.code().toString())
                    }
                } catch (e: HttpException) {
                    Log.e("GetPokemon", e.message())
                } catch (e: Throwable) {
                    Log.e("GetPokemon", "Something went wrong")
                }
            }
        }
    }

    fun capturePokemon(token: String, id: Int, name: String, lat: Double, lon: Double){
        CoroutineScope(Dispatchers.IO).launch {
            val response = repository.capturePokemon("Bearer $token", id, name, lat, lon)
            withContext(Dispatchers.Main) {
                try {
                    if (response.isSuccessful) {
                        capturedPokemon.postValue("success")
                    } else {
                        capturedPokemon.postValue("failed")
                        Log.e("CapturePokemon", response.code().toString())
                    }
                } catch (e: HttpException) {
                    Log.e("CapturePokemon", e.stackTraceToString())
                } catch (e: Throwable) {
                    Log.e("CapturePokemon", "Something went wrong")
                }
            }
        }
    }

    class ViewModelFactory constructor(private val repository: MainRepository) :
        ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(PokemonDetailViewModel::class.java)) {
                PokemonDetailViewModel(this.repository) as T
            } else {
                throw IllegalArgumentException("ViewModel Not Found")
            }
        }
    }
}