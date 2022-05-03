package com.aleksa.samaritanassignment.network

import retrofit2.Call
import retrofit2.http.Field

class MainRepository constructor(private val retrofitService: RetrofitService) {

    suspend fun getToken() = retrofitService.generateToken()
    suspend fun getPokemon(name: String) = retrofitService.getPokemon(name)
    suspend fun getPokemonList(limit: String) = retrofitService.getPokemonList(limit)
    suspend fun getCommunity(header: String) = retrofitService.getCommunity(header)
    suspend fun getMyTeam(header: String) = retrofitService.getMyTeam(header)
    suspend fun getCaptured(header: String) = retrofitService.getCaptured(header)
    suspend fun capturePokemon(header: String, id: Int, name: String, lat: Double, long: Double ) = retrofitService.capturePokemon(header, id, name, lat, long)





}