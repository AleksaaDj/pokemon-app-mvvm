package com.aleksa.samaritanassignment.fragments.explore

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.aleksa.samaritanassignment.PokemonApplication
import com.aleksa.samaritanassignment.R
import com.aleksa.samaritanassignment.activities.PokemonDetailActivity
import com.aleksa.samaritanassignment.databinding.FragmentExploreBinding
import com.aleksa.samaritanassignment.utils.Constants
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import kotlin.random.Random


class ExploreFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private var mapView: MapView? = null
    lateinit var viewModel: ExploreViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentExploreBinding =
            FragmentExploreBinding.inflate(inflater, container, false)
        mapView = binding.mapView
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync(this)
        setupViewModel()
        return binding.root
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this, ExploreViewModel.ViewModelFactory((activity?.application as PokemonApplication).repository
            ),
        ).get(ExploreViewModel::class.java)
        viewModel.tokenLiveData.observe(viewLifecycleOwner) {
            saveToken(it.token)
        }
        viewModel.getToken()
    }

    private fun saveToken(token: String) {
        val sharedPreference =
            activity?.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME_MAIN, Context.MODE_PRIVATE)
        val editor = sharedPreference?.edit()
        editor?.putString(Constants.SHARED_PREFERENCES_TOKEN, token)
        editor?.apply()
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        startActivity(Intent(context, PokemonDetailActivity::class.java).apply {
            putExtra(Constants.SHARED_PREFERENCES_POKEMON_NAME, marker.title)
            putExtra(Constants.SHARED_PREFERENCES_SCREEN_TYPE, PokemonDetailActivity.WILD)
            putExtra(Constants.SHARED_PREFERENCES_POKEMON_LONGITUDE, marker.position.longitude)
            putExtra(Constants.SHARED_PREFERENCES_POKEMON_LATITUDE, marker.position.latitude)
        })
        return true
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.uiSettings.isMyLocationButtonEnabled = true
        try {
            activity?.let { MapsInitializer.initialize(it) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        googleMap.setOnMarkerClickListener(this)
        googleMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    35.6762,
                    139.6503
                ), 17.0f
            )
        )
        viewModel.pokemonListLiveData.observe(viewLifecycleOwner) {
            var latitudeDefault = 35.6774
            var longitudeDefault = 139.6503
            val sliceList = it.pokemonList.slice(1..19)
            sliceList.forEachIndexed { _, pokemonList ->
                addMaker(googleMap, pokemonList.name, latitudeDefault, longitudeDefault)
                latitudeDefault = Random.nextDouble(35.6670, 35.6815)
                longitudeDefault = Random.nextDouble(139.6420, 139.6550)
            }
        }
        viewModel.getPokemonList("30")
    }

    private fun addMaker(googleMap: GoogleMap, pokemonName: String, latitude: Double, longitude: Double) {
        googleMap.addMarker(
            MarkerOptions()
                .position(LatLng(latitude, longitude))
                .title(pokemonName)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pokeball))
        )
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }
}