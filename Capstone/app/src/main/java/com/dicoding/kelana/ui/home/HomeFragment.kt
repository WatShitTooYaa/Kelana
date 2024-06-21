package com.dicoding.kelana.ui.home

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.kelana.R
import com.dicoding.kelana.data.UserModel
import com.dicoding.kelana.data.response.PreferenceWisataResponseItem
import com.dicoding.kelana.databinding.FragmentHomeBinding
import com.dicoding.kelana.db.UserPreference
import com.dicoding.kelana.ui.detail.wisata.DetailWisataActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.Task
import java.io.IOException
import java.util.Locale

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val homeFragmentViewModel: HomeFragmentViewModel by viewModels()
    private lateinit var mUserPreference: UserPreference
    private var user = UserModel()
    private lateinit var adapter: HomeFragmentAdapter
    private lateinit var locationRequest: LocationRequest


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        with(binding){
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    searchBar.setText(searchView.text)
                    searchView.hide()
                    Toast.makeText(root.context, searchView.text, Toast.LENGTH_SHORT).show()
                    false
                }
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        createLocationRequest()
        getLastLocation()


        adapter = HomeFragmentAdapter(Location("dummy"), homeFragmentViewModel)

        mUserPreference = UserPreference(root.context)

//        binding.btnBundleFilter.setOnClickListener {
//            Toast.makeText(context, status, Toast.LENGTH_SHORT).show()
//        }

        homeFragmentViewModel.status.observe(viewLifecycleOwner) { status ->
            status?.let {
                Log.d("HomeFragment", "Status: $status")
                // Update the UI here based on the status
            }
        }

        homeFragmentViewModel.dataWisata.observe(viewLifecycleOwner) { data ->
            data?.let {
                setData(it)
            }
        }

        homeFragmentViewModel.lastLocation.observe(viewLifecycleOwner) { location ->
            location?.let {
                binding.tvFragmentHomeLocation.text = it
            }
        }

        val spinner: Spinner = binding.spinnerCategory
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.sort_options, // Define this array in res/values/strings.xml
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> homeFragmentViewModel.sortByName()
                    1 -> homeFragmentViewModel.sortByDistance()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }


        setupRecyclerView(binding)

//        val layoutManager = GridLayoutManager(context, 2)
//        binding.rvFragmentHome.layoutManager = layoutManager

//        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        return root
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000).apply {
            setMinUpdateDistanceMeters(5000f)
            setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
            setWaitForAccurateLocation(true)
        }.build()
    }

    private fun setData(data: List<PreferenceWisataResponseItem>){
//        val adapter = HomeFragmentAdapter()
        adapter.submitList(data)
        binding.rvFragmentHome.adapter = adapter
        setOnClickItemAdapter(adapter)

        (binding.rvFragmentHome.layoutManager as GridLayoutManager).scrollToPosition(0)
    }

    private fun setOnClickItemAdapter(adapter: HomeFragmentAdapter){
        adapter.setOnClickListener(object : HomeFragmentAdapter.OnClickListener{
            override fun onClick(position: Int, model: PreferenceWisataResponseItem) {
                val intent = Intent(context, DetailWisataActivity::class.java)
                intent.putExtra("PLACE_ID", model.placeId.toString())
                makeToast(model.placeId.toString())
                startActivity(intent)
            }
        })
    }

    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        fusedLocationClient.lastLocation.addOnCompleteListener { task: Task<Location> ->
            if (task.isSuccessful && task.result != null) {
                var addressName: String? = null
                val location = task.result
                val geocoder = Geocoder(requireContext(), Locale.getDefault())
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    geocoder.getFromLocation(location.latitude, location.longitude, 1) { list ->
                        if (list.size != 0) {
                            addressName = list[0].getAddressLine(0)
                            homeFragmentViewModel.setLocation(addressName) // Update ViewModel with location
                            adapter.updateCurrentLocation(location)
                        }
                    }
                } else {
                    try {
                        val list = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        if (list != null && list.size != 0) {
                            addressName = list[0].getAddressLine(0)
                            homeFragmentViewModel.setLocation(addressName) // Update ViewModel with location
                            adapter.updateCurrentLocation(location)
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            } else {
                makeToast("Failed to get location.")
            }
            getWisataPreference()
        }
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request permission jika belum diberikan
            requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val location = locationResult.lastLocation
            if (location != null) {
                // Set current location ke adapter
//                adapter.setCurrentLocation(location)
                fusedLocationClient.removeLocationUpdates(this) // Stop location updates after getting current location
            }
        }
    }

    private fun getWisataPreference(){
        user = mUserPreference.getUser()
        if (user.token != null && user.preference != null){
            makeToast("sedang get")
            homeFragmentViewModel.getWisata(user.token!!, user.preference!!)
        }
//        user.token?.let { token -> user.preference?.let { pref -> status = homeFragmentViewModel.getWisata(token, pref) } } ?: makeToast("user null")
    }

    private fun setupRecyclerView(binding: FragmentHomeBinding) {
        binding.rvFragmentHome.layoutManager = GridLayoutManager(context, 2)
        binding.rvFragmentHome.adapter = adapter
    }

    private fun makeToast(message: String){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        getCurrentLocation()
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}