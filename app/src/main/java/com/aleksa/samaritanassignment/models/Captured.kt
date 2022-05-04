package com.aleksa.samaritanassignment.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class Captured(@SerializedName("captured") val capturedItem: List<CapturedItem>)
@Entity(tableName = "captured_table") data class CapturedItem(@PrimaryKey(autoGenerate = true) val id: Int, val name: String, @SerializedName("captured_at") val capturedAt: String, @SerializedName("captured_lat_at") val capturedLatAt: String, @SerializedName("captured_long_at") val capturedLongAt: String)


