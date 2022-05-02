package com.aleksa.samaritanassignment.network

import com.aleksa.samaritanassignment.models.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface RetrofitService {

    @POST("https://us-central1-samaritan-android-assignment.cloudfunctions.net/token?email=aleksa.ddjordjevic@gmail.com")
    fun generateToken(): Call<Token>
    @GET("https://us-central1-samaritan-android-assignment.cloudfunctions.net/activity")
    fun getCommunity(@Header("Authorization") authHeader: String): Call<Community>
    @GET("https://us-central1-samaritan-android-assignment.cloudfunctions.net/my-team")
    fun getMyTeam(@Header("Authorization") authHeader: String): Call<List<MyTeamItem>>
    @GET("https://us-central1-samaritan-android-assignment.cloudfunctions.net/captured")
    fun getCaptured(@Header("Authorization") authHeader: String): Call<List<CapturedItem>>
    @GET("pokemon/{id}")
    fun getPokemon(@Path("id") pokemonId: String): Call<Pokemon>

    companion object {

        var retrofitService: RetrofitService? = null

        fun getInstance() : RetrofitService {

            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://pokeapi.co/api/v2/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(RetrofitService::class.java)
            }
            return retrofitService!!
        }
    }
}