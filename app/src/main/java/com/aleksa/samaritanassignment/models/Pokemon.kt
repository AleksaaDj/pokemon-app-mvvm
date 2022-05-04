package com.aleksa.samaritanassignment.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Pokemon(var id: Int, val name: String, val types: List<TypesDefinition>, val moves: List<MoveDefinition>, val captured_at: String, val sprites: Sprites) : Serializable
data class MoveDefinition(val move: Move)
data class Move(val name: String)
data class TypesDefinition(val type: Type)
data class Type(val name: String)
data class Sprites(@SerializedName("back_default") val backSpriteUrl: String, @SerializedName("front_default") val frontSpriteUrl: String)
data class PokemonList(@SerializedName("results") val pokemonList: List<Pokemon>)