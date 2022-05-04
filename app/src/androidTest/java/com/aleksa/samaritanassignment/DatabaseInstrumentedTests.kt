package com.aleksa.samaritanassignment

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.aleksa.samaritanassignment.models.CapturedItem
import com.aleksa.samaritanassignment.models.MyTeamItem
import com.aleksa.samaritanassignment.network.RoomPokemonDao
import com.aleksa.samaritanassignment.network.RoomPokemonDatabase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import org.junit.Before

@RunWith(AndroidJUnit4::class)
class DatabaseInstrumentedTests {
    private lateinit var pokemonDao: RoomPokemonDao
    private lateinit var db: RoomPokemonDatabase


    @Before
    fun createDatabase() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, RoomPokemonDatabase::class.java
        ).build()
        pokemonDao = db.roomPokemonDao()
    }

    @After
    fun after() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun test_insert_captured_pokemon() = runBlocking {
        val capturedItem = CapturedItem(5, "TestPokemon", "1997-04-01T13:43:27-06:00", "22.0", "55.0")
        pokemonDao.insertCapturedItem(capturedItem)
        val getCaptured = pokemonDao.getCaptured()
        assertEquals(getCaptured[0].name, "TestPokemon")
        assertEquals(getCaptured[0].capturedAt, "1997-04-01T13:43:27-06:00")
        assertEquals(getCaptured[0].capturedLatAt, "22.0")
        assertEquals(getCaptured[0].capturedLongAt, "55.0")
    }

    @Test
    @Throws(Exception::class)
    fun test_insert_my_team_item() = runBlocking {
        val myTeamItem = MyTeamItem(5, "TestTrainer", "1995-04-01T13:43:27-06:00", "38.0", "65.0")
        pokemonDao.insertMyTeamItem(myTeamItem)
        val getMyTeam = pokemonDao.getMyTeam()
        assertEquals(getMyTeam[0].name, "TestTrainer")
        assertEquals(getMyTeam[0].capturedAt, "1995-04-01T13:43:27-06:00")
        assertEquals(getMyTeam[0].capturedLatAt, "38.0")
        assertEquals(getMyTeam[0].capturedLongAt, "65.0")
    }
}