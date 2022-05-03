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
import com.aleksa.samaritanassignment.R
import com.aleksa.samaritanassignment.databinding.ActivityPokemonDetailBinding
import com.aleksa.samaritanassignment.models.Pokemon
import com.aleksa.samaritanassignment.network.MainRepository
import com.aleksa.samaritanassignment.network.RetrofitService
import com.bumptech.glide.Glide
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class PokemonDetailActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var screenType: String
    private lateinit var pokemonName: String
    private var pokemonLongitude: Double = 0.0
    private var pokemonLatitude: Double = 0.0
    lateinit var viewModel: PokemonDetailViewModel
    lateinit var pokemon: Pokemon
    private var mapView: MapView? = null
    private lateinit var token: String
    private val retrofitService = RetrofitService.getInstance()
    private lateinit var binding: ActivityPokemonDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokemonDetailBinding.inflate(layoutInflater)
        val view = binding.root
        supportActionBar?.hide()
        setContentView(view)
        screenType = intent.getStringExtra("screenType").toString()
        pokemonName = intent.getStringExtra("pokemonName").toString()
        pokemonLongitude = intent.getDoubleExtra("pokemonLongitude", 0.0)
        pokemonLatitude = intent.getDoubleExtra("pokemonLatitude", 0.0)
        mapView = binding.mapView
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync(this)
        val sharedPreference =
            applicationContext?.getSharedPreferences("SAMARITAN_PREFERENCE", Context.MODE_PRIVATE)
        token = sharedPreference?.getString("token", "").toString()
        setupViewModel()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this, PokemonDetailViewModel.ViewModelFactory(
                MainRepository(retrofitService)
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
                Toast.makeText(applicationContext, "Failed to capture", Toast.LENGTH_LONG).show()
                startAnimationPokeball()
            }
        }
        viewModel.getPokemon(pokemonName)
    }

    private fun startAnimationPokeball(){
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
        val types = pokemon.types
        val moves = pokemon.moves
        binding.collapsingToolbarLayout.title = pokemonName
        for (i in types.indices) {
            binding.types.append(types[i].type.name)
            binding.types.append(" ")
        }
        binding.move1.text = moves[0].move.name
        binding.move2.text = moves[1].move.name
        binding.move3.text = moves[2].move.name
        binding.move4.text = moves[3].move.name
        Glide.with(baseContext).load(pokemon.sprites.frontSpriteUrl).into(binding.pokemonFrontIv)
        Glide.with(baseContext).load(pokemon.sprites.backSpriteUrl).into(binding.pokemonBackIv)
        binding.captureBtn.isVisible = true

    }

    private fun setupCapturedScreen() {

    }

    private fun setupCapturedOtherScreen() {

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