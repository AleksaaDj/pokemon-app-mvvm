package com.aleksa.samaritanassignment.network

import com.aleksa.samaritanassignment.models.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface RetrofitService {

    @POST("https://us-central1-samaritan-android-assignment.cloudfunctions.net/token?email=aleksa.ddjordjevic@gmail.com")
    suspend fun generateToken(): Response<Token>
    @FormUrlEncoded
    @POST("https://us-central1-samaritan-android-assignment.cloudfunctions.net/capture/pokemon")
    suspend fun capturePokemon(@Header("Authorization") authHeader: String, @Field("id") id: Int, @Field("name") name: String, @Field("lat") lat: Double, @Field("long") long: Double ): Response<String>
    @GET("https://us-central1-samaritan-android-assignment.cloudfunctions.net/activity")
    suspend fun getCommunity(@Header("Authorization") authHeader: String): Response<Community>
    @GET("https://us-central1-samaritan-android-assignment.cloudfunctions.net/my-team")
    suspend fun getMyTeam(@Header("Authorization") authHeader: String): Response<List<MyTeamItem>>
    @GET("https://us-central1-samaritan-android-assignment.cloudfunctions.net/captured")
    suspend fun getCaptured(@Header("Authorization") authHeader: String): Response<List<CapturedItem>>
    @GET("pokemon/{name}")
    suspend fun getPokemon(@Path("name") pokemonName: String): Response<Pokemon>
    @GET("pokemon?limit")
    suspend fun getPokemonList(@Query("limit") limit: String): Response<PokemonList>

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