package com.aleksa.samaritanassignment.network

import android.content.Context
import androidx.room.*
import com.aleksa.samaritanassignment.models.CapturedItem
import com.aleksa.samaritanassignment.models.MyTeamItem


@Database(entities = [MyTeamItem::class, CapturedItem::class], version = 1, exportSchema = false)
abstract class RoomPokemonDatabase : RoomDatabase() {

    abstract fun roomPokemonDao() : RoomPokemonDao

    companion object{
        @Volatile
        private var INSTANCE : RoomPokemonDatabase? = null

        fun getDatabase(context: Context): RoomPokemonDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RoomPokemonDatabase::class.java, "pokemon_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}