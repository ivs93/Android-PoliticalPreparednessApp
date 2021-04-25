package com.example.android.politicalpreparedness.representative

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.BuildConfig
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.material.snackbar.Snackbar
import java.util.Locale

class DetailFragment : Fragment() {

    companion object {
        //TODO: Add Constant for Location request
        const val REQUEST_LOCATION_PERMISSION_REQUEST_CODE = 1
        const val REQUEST_TURN_DEVICE_LOCATION_ON = 29
        private const val TAG = "RepresentativeFragment"
    }

    private lateinit var binding : FragmentRepresentativeBinding

    //TODO: Declare ViewModel
    private val viewModel: RepresentativeViewModel by lazy {
        ViewModelProvider(this).get(RepresentativeViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentRepresentativeBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        binding.recyclerRepresentatives.adapter = RepresentativeListAdapter()

        viewModel.stateIndex.observe(viewLifecycleOwner, Observer {
            if ( null != it ) {
                viewModel.getState(resources.getStringArray(R.array.states)[it])
            }
        })

        viewModel.showToast.observe(viewLifecycleOwner, Observer {
                Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
        })

        binding.buttonLocation.setOnClickListener { checkLocationPermissions() }

        binding.buttonSearch.setOnClickListener {
            hideKeyboard()
            viewModel.findRepresentatives()
        }


        //TODO: Establish bindings

        //TODO: Define and assign Representative adapter

        //TODO: Populate Representative adapter

        //TODO: Establish button listeners for field and location search

        return binding.root
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //TODO: Handle location permission result to get location on permission granted
        when (requestCode) {
            REQUEST_LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isEmpty() ||
                        grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    /*
                    * We should explain here why we need to access to the user location
                    * */
                    Snackbar.make(
                            binding.motionLayout,
                            R.string.location_required_error,
                            Snackbar.LENGTH_INDEFINITE
                    ).setAction(R.string.settings) {
                        startActivity(Intent().apply {
                            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        })
                    }.show()
                } else {
                    checkDeviceLocation()

                }
            }
            REQUEST_TURN_DEVICE_LOCATION_ON -> {
                checkDeviceLocation(false)
            }
        }
    }

    private fun checkLocationPermissions(): Boolean {
        return if (isPermissionGranted()) {
            checkDeviceLocation()
            true
        } else {
            //TODO: Request Location permissions
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_LOCATION_PERMISSION_REQUEST_CODE)
            false
        }
    }

    private fun isPermissionGranted() : Boolean {
        //TODO: Check if permission is already granted and return (true = granted, false = denied/other)
        return (
                PackageManager.PERMISSION_GRANTED ==
                        ActivityCompat.checkSelfPermission(
                                requireContext(),
                                Manifest.permission.ACCESS_FINE_LOCATION
                        ))
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        //TODO: Get location from LocationServices
        //TODO: The geoCodeLocation method is a helper function to change the lat/long location to a human readable street address
        val fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(requireContext())
        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            if (it != null) {
                val address = geoCodeLocation(it)
                viewModel.line1.value = address.line1
                viewModel.line2.value = address.line2
                viewModel.city.value = address.city
                viewModel.zip.value = address.zip
                val states = resources.getStringArray(R.array.states)
                val selectedStateIndex = states.indexOf(address.state)
                if (selectedStateIndex != -1) {
                    binding.state.setSelection(selectedStateIndex)
                }
                viewModel.findRepresentatives()
            } else {
                Log.d(TAG, "location null" )
            }
        }
                .addOnFailureListener {
                    Log.d(TAG, "Failure" )
                }

    }

    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
                .map { address ->
                    Address(address.thoroughfare, address.subThoroughfare, address.locality, address.adminArea, address.postalCode)
                }
                .first()
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
    }

    private fun checkDeviceLocation(resolve: Boolean = true) {
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_LOW_POWER
        }
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val settingsClient = LocationServices.getSettingsClient(requireActivity())
        val locationSettingsResponseTask =
                settingsClient.checkLocationSettings(builder.build())
        locationSettingsResponseTask.addOnFailureListener { exception ->
            if (exception is ResolvableApiException && resolve) {
                try {
                    exception.startResolutionForResult(
                            requireActivity(),
                            REQUEST_TURN_DEVICE_LOCATION_ON
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    Log.d(TAG, "Error getting location settings resolution: " + sendEx.message)
                }
            } else {
                Snackbar.make(
                        binding.motionLayout,
                        R.string.location_required_error, Snackbar.LENGTH_INDEFINITE
                ).setAction(android.R.string.ok) {
                    checkDeviceLocation()
                }.show()
            }
        }
        locationSettingsResponseTask.addOnCompleteListener {
            if (it.isSuccessful) {
                getLocation()
            }
        }
    }


}