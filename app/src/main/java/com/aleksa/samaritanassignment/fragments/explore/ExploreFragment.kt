package com.aleksa.samaritanassignment.fragments.explore

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.aleksa.samaritanassignment.R
import com.aleksa.samaritanassignment.databinding.FragmentExploreBinding
import com.aleksa.samaritanassignment.models.MoveDefinition
import com.aleksa.samaritanassignment.network.MainRepository
import com.aleksa.samaritanassignment.network.RetrofitService
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*


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
    private fun setupViewModel(){
        viewModel = ViewModelProvider(
            this, ExploreViewModel.ViewModelFactory(
                MainRepository(retrofitService)
            )
        ).get(ExploreViewModel::class.java)
        viewModel.tokenLiveData.observe(viewLifecycleOwner) {
            saveToken(it.token)
        }
        viewModel.pokemonLiveData.observe(viewLifecycleOwner) {
            showMoves(it.moves)
            Log.d("TAG", "onCreate: ${it.moves[2]}")
        }
        viewModel.getToken()
        viewModel.getPokemon("55")
    }

    private fun saveToken(token: String) {
        val sharedPreference =
            activity?.getSharedPreferences("SAMARITAN_PREFERENCE", Context.MODE_PRIVATE)
        val editor = sharedPreference?.edit()
        editor?.putString("token", token)
        editor?.apply()
    }

    private fun showMoves(list: List<MoveDefinition>) {
        val sliceList = list.slice(1..4)
        sliceList.forEachIndexed { _, moveDefinition ->
            Log.d("MOVENAME", moveDefinition.move.name)
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        Toast.makeText(context, marker.title + " is captured", Toast.LENGTH_LONG).show()
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
        addMaker(googleMap, "Pikachu", 35.6774, 139.6503)
        addMaker(googleMap, "Bumbasaur", 35.6763, 139.6518)
        addMaker(googleMap, "Togepi", 35.6752, 139.6500)
        addMaker(googleMap, "Minosaur", 35.6741, 139.65024)
        addMaker(googleMap, "Squirrel", 35.6762, 139.6503)

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