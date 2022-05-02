package com.aleksa.samaritanassignment.models

import java.io.Serializable

data class Pokemon(var id: Int, val name: String, val captured_at: String, val moves: List<MoveDefinition>) : Serializable
data class MoveDefinition(val move: Move)
data class Move(val name: String)
