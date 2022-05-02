package com.aleksa.samaritanassignment.models

import com.google.gson.annotations.SerializedName

data class Captured(@SerializedName("captured") val capturedItem: List<CapturedItem>)
data class CapturedItem(val id: Int, val name: String, val captured_at: String, val captured_lat_at: String, val captured_long_at: String)


