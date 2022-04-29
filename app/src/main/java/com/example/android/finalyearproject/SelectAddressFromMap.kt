package com.example.android.finalyearproject

import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.android.finalyearproject.databinding.FragmentSelectAddressFromMapBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.maps.CameraUpdateFactory
import com.google.android.libraries.maps.GoogleMap
import com.google.android.libraries.maps.SupportMapFragment
import com.google.android.libraries.maps.model.LatLng
import com.google.android.libraries.maps.model.Marker
import com.google.android.libraries.maps.model.MarkerOptions
import java.io.IOException

class SelectAddressFromMap(
    private val selectAddressFromMapListener: SelectAddressFromMapListener
) : Fragment(), GoogleMap.OnMarkerClickListener {

    private var _binding: FragmentSelectAddressFromMapBinding? = null
    private val binding get() = _binding!!

    private var googleMap: GoogleMap? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private lateinit var titleLocation: kotlin.String
    private lateinit var btnSubmitAddress: Button

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSelectAddressFromMapBinding.inflate(layoutInflater)
        fusedLocationClient = LocationServices
            .getFusedLocationProviderClient(requireActivity())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initGoogleMap()
        btnSubmitAddress.setOnClickListener {
            selectAddressFromMapListener.selectAddressComplete(titleLocation)
            requireActivity().onBackPressed()
        }
    }

    private fun initGoogleMap() {
        getMapFragment(childFragmentManager)?.let { mapFragment ->
            val supportMapFragment = mapFragment as? SupportMapFragment
            supportMapFragment?.getMapAsync { googleMap: GoogleMap ->
                onMapReady(googleMap)
            }
        }
    }

    private fun getMapFragment(fragmentManager: FragmentManager): SupportMapFragment? =
        fragmentManager.findFragmentById(R.id.mapFragmentSelectAddressFromMap) as? SupportMapFragment

    private fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.setOnMarkerClickListener(this)
        setUpMap()
        googleMap.setOnMapClickListener {
            googleMap.clear()
            placeMarkerOnMap(it)
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(it))
        }
    }

    private fun setUpMap(): Boolean {
        if (ActivityCompat.checkSelfPermission(requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE)

            return true
        }

        googleMap?.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener(requireActivity()) { location ->
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                placeMarkerOnMap(currentLatLng)
                googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 16f))
            }
        }

        return false
    }

    private fun placeMarkerOnMap(location: LatLng) {
        val markerOptions = MarkerOptions().position(location)
        titleLocation = getAddress(location)
        markerOptions.title(titleLocation)
        googleMap?.addMarker(markerOptions)
    }

    private fun getAddress(latLng: LatLng): kotlin.String {
        val geocoder = Geocoder(requireContext())
        val address: Address?
        var addressText = ""
        try {
            val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            if (addresses != null && addresses.isNotEmpty()) {
                address = addresses[0]
                addressText = address.subThoroughfare + " " + address.thoroughfare + " " + address.subLocality + " " + address.adminArea + " " + address.countryName
            }
        } catch (e: IOException) {
            Log.e("MapsActivity", e.localizedMessage)
        }

        return addressText
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        return false
    }

}