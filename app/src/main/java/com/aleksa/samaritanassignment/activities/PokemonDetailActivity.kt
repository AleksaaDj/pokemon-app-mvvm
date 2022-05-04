package com.aleksa.samaritanassignment.activities

import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.aleksa.samaritanassignment.PokemonApplication
import com.aleksa.samaritanassignment.R
import com.aleksa.samaritanassignment.databinding.ActivityPokemonDetailBinding
import com.aleksa.samaritanassignment.models.Pokemon
import com.aleksa.samaritanassignment.utils.Constants
import com.bumptech.glide.Glide
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class PokemonDetailActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var screenType: String
    private lateinit var pokemonName: String
    private lateinit var trainerName: String
    private lateinit var captureTime: String
    private var pokemonLongitude: Double = 0.0
    private var pokemonLatitude: Double = 0.0
    lateinit var viewModel: PokemonDetailViewModel
    lateinit var pokemon: Pokemon
    private var mapView: MapView? = null
    private lateinit var token: String
    private lateinit var binding: ActivityPokemonDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokemonDetailBinding.inflate(layoutInflater)
        val view = binding.root
        supportActionBar?.hide()
        setContentView(view)
        getSharedPreferencesValues()
        mapView = binding.mapView
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync(this)
        val sharedPreference =
            applicationContext?.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME_MAIN, Context.MODE_PRIVATE)
        token = sharedPreference?.getString(Constants.SHARED_PREFERENCES_TOKEN, "").toString()
        setupViewModel()
    }

    private fun getSharedPreferencesValues(){
        screenType = intent.getStringExtra(Constants.SHARED_PREFERENCES_SCREEN_TYPE).toString()
        pokemonName = intent.getStringExtra(Constants.SHARED_PREFERENCES_POKEMON_NAME).toString()
        trainerName = intent.getStringExtra(Constants.SHARED_PREFERENCES_POKEMON_TRAINER_NAME).toString()
        captureTime = intent.getStringExtra(Constants.SHARED_PREFERENCES_POKEMON_CAPTURED_AT).toString()
        pokemonLongitude = intent.getDoubleExtra(Constants.SHARED_PREFERENCES_POKEMON_LONGITUDE, 0.0)
        pokemonLatitude = intent.getDoubleExtra(Constants.SHARED_PREFERENCES_POKEMON_LATITUDE, 0.0)
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this, PokemonDetailViewModel.ViewModelFactory(
                (application as PokemonApplication).repository
            )
        ).get(PokemonDetailViewModel::class.java)
        viewModel.pokemonLiveData.observe(this) {
            pokemon = it
            setupViews()
        }
        viewModel.capturedPokemon.observe(this) {
            if (it == "success") {
                Toast.makeText(applicationContext, "Captured!", Toast.LENGTH_LONG).show()
                startAnimationPokeball()
            } else {
                Toast.makeText(applicationContext, "Fail to capture", Toast.LENGTH_LONG).show()
                startAnimationPokeball()
            }
        }
        viewModel.getPokemon(pokemonName)
    }

    private fun startAnimationPokeball() {
        binding.animationView.animate().alpha(0.0f).duration = 3000
        if (!binding.animationView.isAnimating) {
            binding.animationView.pauseAnimation()
            binding.animationView.isVisible = false
        }
    }

    private fun setupViews() {
        setupCaptureButton()
        when (screenType) {
            WILD -> {
                setupWildScreen()
            }
            CAPTURED -> {
                setupCapturedScreen()
            }
            CAPTURED_BY_OTHER -> {
                setupCapturedOtherScreen()
            }
        }
    }

    private fun setupWildScreen() {
        binding.pokeballAnchor.isVisible = false
        binding.trainerCardView.isVisible = false
        binding.captureBtn.isVisible = true
        binding.timeCapturedTv.isVisible = false
        binding.foundTitle.text = getString(R.string.found_in)
        setupDefaultViews()
    }

    private fun setupCapturedScreen() {
        binding.pokeballAnchor.isVisible = true
        binding.trainerCardView.isVisible = false
        binding.captureBtn.isVisible = false
        binding.timeCapturedTv.isVisible = false
        binding.foundTitle.text = getString(R.string.capture_in)
        setupDefaultViews()
    }

    private fun setupCapturedOtherScreen() {
        binding.pokeballAnchor.isVisible = false
        binding.trainerCardView.isVisible = true
        binding.captureBtn.isVisible = false
        binding.timeCapturedTv.isVisible = true
        binding.mapCardView.isVisible = false
        binding.trainerNameTv.text = getString(R.string.captured_by_placeholder, trainerName)
        binding.timeCapturedTv.text = getString(R.string.captured_on_placeholder, captureTime)
        setupDefaultViews()
    }

    private fun setupDefaultViews() {
        val types = pokemon.types
        val moves = pokemon.moves
        binding.collapsingToolbarLayout.title = pokemon.name
        for (i in types.indices) {
            binding.types.append(" ")
            binding.types.append(types[i].type.name)
        }
        binding.move1.text = moves[0].move.name
        binding.move2.text = moves[1].move.name
        binding.move3.text = moves[2].move.name
        binding.move4.text = moves[3].move.name
        Glide.with(baseContext).load(pokemon.sprites.frontSpriteUrl).into(binding.pokemonFrontIv)
        Glide.with(baseContext).load(pokemon.sprites.backSpriteUrl).into(binding.pokemonBackIv)
    }

    private fun setupCaptureButton() {
        binding.captureBtn.setOnClickListener {
            val customDialog = Dialog(this@PokemonDetailActivity)
            customDialog.setContentView(R.layout.custom_dialog_capture)
            customDialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            val editText = customDialog.findViewById(R.id.txt_input) as EditText
            val saveBtn = customDialog.findViewById(R.id.btn_save) as Button
            val cancelBtn = customDialog.findViewById(R.id.btn_cancel) as Button
            saveBtn.setOnClickListener {
                viewModel.capturePokemon(
                    token,
                    pokemon.id,
                    editText.text.toString(),
                    pokemonLatitude,
                    pokemonLongitude
                )
                customDialog.dismiss()
                binding.animationView.isVisible = true
                binding.animationView.playAnimation()
            }
            cancelBtn.setOnClickListener {
                customDialog.dismiss()
            }
            customDialog.show()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.uiSettings.isMyLocationButtonEnabled = true
        try {
            baseContext.let { MapsInitializer.initialize(it) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        googleMap.addMarker(
            MarkerOptions()
                .position(LatLng(pokemonLatitude, pokemonLongitude))
                .title(pokemonName)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pokeball))
        )
        googleMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    pokemonLatitude,
                    pokemonLongitude
                ), 19.0f
            )
        )
    }

    companion object ScreenType {
        const val WILD = "WILD"
        const val CAPTURED = "CAPTURED"
        const val CAPTURED_BY_OTHER = "CAPTURED_BY_OTHER"
    }
}