package com.aleksa.samaritanassignment.models

import com.google.gson.annotations.SerializedName

data class MyTeam(@SerializedName("my-team") val myTeamItem: List<MyTeamItem>)
data class MyTeamItem(val id: Int, val name: String, val captured_at: String, val captured_lat_at: String, val captured_long_at: String)