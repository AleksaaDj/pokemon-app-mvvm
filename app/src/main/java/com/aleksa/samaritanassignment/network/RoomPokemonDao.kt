package com.aleksa.samaritanassignment.network

import androidx.room.*
import com.aleksa.samaritanassignment.models.CapturedItem
import com.aleksa.samaritanassignment.models.MyTeamItem

@Dao
interface RoomPokemonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMyTeamItem(myTeamItem: MyTeamItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCapturedItem(capturedItem: CapturedItem)

    @Query("SELECT * FROM my_team_table")
    fun getMyTeam() : List<MyTeamItem>

    @Query("SELECT * FROM captured_table ")
    fun getCaptured() : List<CapturedItem>

}