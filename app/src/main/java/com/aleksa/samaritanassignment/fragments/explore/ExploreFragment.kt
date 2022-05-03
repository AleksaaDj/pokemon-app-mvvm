package com.aleksa.samaritanassignment.fragments.explore

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.aleksa.samaritanassignment.R
import com.aleksa.samaritanassignment.activities.PokemonDetailActivity
import com.aleksa.samaritanassignment.databinding.FragmentExploreBinding
import com.aleksa.samaritanassignment.network.MainRepository
import com.aleksa.samaritanassignment.network.RetrofitService
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import kotlin.random.Random


class ExploreFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private var mapView: MapView? = null
    lateinit var viewModel: ExploreViewModel
    private val retrofitService = RetrofitService.getInstance()

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
            this, ExploreViewModel.ViewModelFactory(
                MainRepository(retrofitService)
            )
        ).get(ExploreViewModel::class.java)
        viewModel.tokenLiveData.observe(viewLifecycleOwner) {
            saveToken(it.token)
        }
        viewModel.getToken()
    }

    private fun saveToken(token: String) {
        val sharedPreference =
            activity?.getSharedPreferences("SAMARITAN_PREFERENCE", Context.MODE_PRIVATE)
        val editor = sharedPreference?.edit()
        editor?.putString("token", token)
        editor?.apply()
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        startActivity(Intent(context, PokemonDetailActivity::class.java).apply {
            putExtra("pokemonName", marker.title)
            putExtra("screenType", PokemonDetailActivity.WILD)
            putExtra("pokemonLongitude", marker.position.longitude)
            putExtra("pokemonLatitude", marker.position.latitude)
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
            var latitude = 35.6774
            var longitude = 139.6503
            val sliceList = it.pokemonList.slice(1..19)
            sliceList.forEachIndexed { _, pokemonList ->
                addMaker(googleMap, pokemonList.name, latitude, longitude)
                latitude = Random.nextDouble(35.6670, 35.6815)
                longitude = Random.nextDouble(139.6420, 139.6550)
            }
        }
        viewModel.getPokemonList("30")
    }

    private fun addMaker(
        googleMap: GoogleMap,
        pokemonName: String,
        latitude: Double,
        longitude: Double
    ) {
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