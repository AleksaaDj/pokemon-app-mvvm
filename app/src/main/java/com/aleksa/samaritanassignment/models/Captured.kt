package com.aleksa.samaritanassignment.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class Captured(@SerializedName("captured") val capturedItem: List<CapturedItem>)
@Entity(tableName = "captured_table") data class CapturedItem(@PrimaryKey(autoGenerate = true) val id: Int, val name: String, val captured_at: String, val captured_lat_at: String, val captured_long_at: String)


