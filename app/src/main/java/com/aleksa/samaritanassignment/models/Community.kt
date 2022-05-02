package com.aleksa.samaritanassignment.models

data class Community(val friends: List<Friends>, val foes: List<Foes>)
data class Friends(val name: String, val pokemon: Pokemon)
data class Foes(val name: String, val pokemon: Pokemon)