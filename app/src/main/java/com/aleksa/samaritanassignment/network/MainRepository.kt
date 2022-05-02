package com.aleksa.samaritanassignment.network

class MainRepository constructor(private val retrofitService: RetrofitService) {

    fun getToken() = retrofitService.generateToken()
    fun getPokemon(id: String) = retrofitService.getPokemon(id)
    fun getCommunity(header: String) = retrofitService.getCommunity(header)
    fun getMyTeam(header: String) = retrofitService.getMyTeam(header)
    fun getCaptured(header: String) = retrofitService.getCaptured(header)




}