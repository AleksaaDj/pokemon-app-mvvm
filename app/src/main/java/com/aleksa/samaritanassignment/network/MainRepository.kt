package com.aleksa.samaritanassignment.network

import androidx.annotation.WorkerThread
import com.aleksa.samaritanassignment.models.CapturedItem
import com.aleksa.samaritanassignment.models.MyTeamItem
import okhttp3.RequestBody


class MainRepository(
    private val retrofitService: RetrofitService,
    private val roomPokemonDatabase: RoomPokemonDatabase
) {

    //Database calls
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertMyTeamItem(myTeamItem: MyTeamItem) {
        roomPokemonDatabase.roomPokemonDao().insertMyTeamItem(myTeamItem)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertCapturedItem(capturedItem: CapturedItem) {
        roomPokemonDatabase.roomPokemonDao().insertCapturedItem(capturedItem)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getMyItemTable(): List<MyTeamItem> {
        return roomPokemonDatabase.roomPokemonDao().getMyTeam()
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getCapturedTable(): List<CapturedItem> {
        return roomPokemonDatabase.roomPokemonDao().getCaptured()
    }

    //API Calls
    suspend fun getToken() = retrofitService.generateToken()
    suspend fun getPokemon(name: String) = retrofitService.getPokemon(name)
    suspend fun getPokemonList(limit: String) = retrofitService.getPokemonList(limit)
    suspend fun getCommunity(header: String) = retrofitService.getCommunity(header)
    suspend fun getMyTeam(header: String) = retrofitService.getMyTeam(header)
    suspend fun getCaptured(header: String) = retrofitService.getCaptured(header)
    suspend fun capturePokemon(header: String, requestBody: RequestBody) =
        retrofitService.capturePokemon(header, requestBody)


}