package com.aleksa.samaritanassignment.fragments.explore

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aleksa.samaritanassignment.models.Pokemon
import com.aleksa.samaritanassignment.models.Token
import com.aleksa.samaritanassignment.network.MainRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ExploreViewModel constructor(private val repository: MainRepository) : ViewModel() {

    val pokemonLiveData = MutableLiveData<Pokemon>()
    val tokenLiveData = MutableLiveData<Token>()

    fun getToken(){
        val response = repository.getToken()
        response.enqueue(object : Callback<Token> {
            override fun onResponse(call: Call<Token>, response: Response<Token>) {
                tokenLiveData.postValue(response.body())
            }
            override fun onFailure(call: Call<Token>, t: Throwable) {
                Log.e("GetToken",t.message.toString())
            }
        })
    }
     fun getPokemon(id: String){
        val response = repository.getPokemon(id)
        response.enqueue(object : Callback<Pokemon> {
            override fun onResponse(call: Call<Pokemon>, response: Response<Pokemon>) {
               /* val responseBody = response.body()
                val pokemon: Pokemon? = responseBody?.let { Pokemon(it.id, it.name, it.moves) }
                pokemonLiveData.postValue(pokemon)*/
            }
            override fun onFailure(call: Call<Pokemon>, t: Throwable) {
                Log.e("GetPokemon",t.message.toString())
            }
        })
    }

    class ViewModelFactory constructor(private val repository: MainRepository) :
        ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(ExploreViewModel::class.java)) {
                ExploreViewModel(this.repository) as T
            } else {
                throw IllegalArgumentException("ViewModel Not Found")
            }
        }
    }
}