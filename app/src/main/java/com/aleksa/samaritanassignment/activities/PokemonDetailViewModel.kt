package com.aleksa.samaritanassignment.activities

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aleksa.samaritanassignment.models.CapturedItem
import com.aleksa.samaritanassignment.models.Pokemon
import com.aleksa.samaritanassignment.network.MainRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.HttpException

class PokemonDetailViewModel constructor(private val repository: MainRepository) : ViewModel() {

    val capturedPokemon = MutableLiveData<String>()
    val pokemonLiveData = MutableLiveData<Pokemon>()

    fun getPokemon(name: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = repository.getPokemon(name)
            withContext(Dispatchers.Main) {
                try {
                    if (response.isSuccessful) {
                        pokemonLiveData.postValue(response.body())
                    } else {
                        Log.e(TAG_GET_POKEMON, response.code().toString())
                    }
                } catch (e: HttpException) {
                    Log.e(TAG_GET_POKEMON, e.message())
                } catch (e: Throwable) {
                    Log.e(TAG_GET_POKEMON, "Something went wrong")
                }
            }
        }
    }

    fun capturePokemon(token: String, id: Int, name: String, lat: Double, lon: Double) {
        CoroutineScope(Dispatchers.IO).launch {
            val requestBody = prepareRequestBody(id, name, lat, lon)
            val response = repository.capturePokemon("Bearer $token", requestBody)
            withContext(Dispatchers.Main) {
                try {
                    if (response.isSuccessful) {
                        val capturedPokemonRoom =
                            CapturedItem(id, name, "", lat.toString(), lon.toString())
                        addCapturedPokemonToDatabase(capturedPokemonRoom)
                        capturedPokemon.postValue(SUCCESS_MSG)
                    } else {
                        capturedPokemon.postValue(ERROR_MSG)
                        Log.e(TAG_CAPTURE_POKEMON, response.code().toString())
                    }
                } catch (e: HttpException) {
                    Log.e(TAG_CAPTURE_POKEMON, e.stackTraceToString())
                } catch (e: Throwable) {
                    Log.e(TAG_CAPTURE_POKEMON, "Something went wrong")
                }
            }
        }
    }

    private fun prepareRequestBody(id: Int, name: String, lat: Double, lon: Double): RequestBody {
        val mainJsonObject = JSONObject()
        val jsonObject = JSONObject()
        jsonObject.put("id", id)
        jsonObject.put("name", name)
        jsonObject.put("lat", lat)
        jsonObject.put("long", lon)
        mainJsonObject.put("pokemon", jsonObject)
        val jsonObjectString = mainJsonObject.toString()

        return jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())
    }

    private suspend fun addCapturedPokemonToDatabase(capturedPokemonRoom: CapturedItem){
        repository.insertCapturedItem(capturedPokemonRoom)
    }

    @Suppress("UNCHECKED_CAST")
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

    companion object {
        const val TAG_CAPTURE_POKEMON = "CapturePokemon"
        const val TAG_GET_POKEMON = "GetPokemon"
        const val SUCCESS_MSG = "success"
        const val ERROR_MSG = "failed"
    }
}