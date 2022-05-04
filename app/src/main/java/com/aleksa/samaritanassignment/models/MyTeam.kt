package com.aleksa.samaritanassignment.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class MyTeam(@SerializedName("my-team") val myTeamItem: List<MyTeamItem>)
@Entity(tableName = "my_team_table") data class MyTeamItem(@PrimaryKey(autoGenerate = true) val id: Int, val name: String, val captured_at: String, val captured_lat_at: String, val captured_long_at: String)