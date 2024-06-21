package com.dicoding.kelana

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.kelana.custom.ButtonLogin
import com.dicoding.kelana.data.UserModel
import com.dicoding.kelana.data.api.ApiConfig
import com.dicoding.kelana.data.api.ApiService
import com.dicoding.kelana.data.api.LoginRequest
import com.dicoding.kelana.data.response.LoginResponse
import com.dicoding.kelana.databinding.ActivityMainBinding
import com.dicoding.kelana.db.UserPreference
import com.dicoding.kelana.preferences.PreferenceActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mUserPreference: UserPreference
    private var user = UserModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        mUserPreference = UserPreference(this)



        binding.btnMvRegister.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
//            val intent = Intent(this, PreferenceActivity::class.java)
//            startActivity(intent)
            val email = binding.etEmail.text.toString()
            val pass = binding.etPassword.text.toString()
            postLogin(email, pass)
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//        checkPermission()
        getMyLocation()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getLastLocation()
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun getLastLocation(){
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
        if (mUserPreference.isUserLoggedIn()){
            val user = mUserPreference.getUser()
            user.email?.let { email -> user.password?.let { pass -> postLogin(email, pass) } }
        }
//        fusedLocationClient.lastLocation.addOnCompleteListener { task: Task<Location> ->
//            if (task.isSuccessful && task.result != null) {
//                val location = task.result
//                val geocoder = Geocoder(this, Locale.getDefault())
//                val addresses: List<Address> =
//                    geocoder.getFromLocation(location.latitude, location.longitude, 1)!!
//                val address: Address = addresses[0]
//                val addressLine = address.getAddressLine(0)
//
//                Toast.makeText(this, "Current location: $addressLine", Toast.LENGTH_LONG).show()
//            } else {
//                Toast.makeText(this, "Failed to get location.", Toast.LENGTH_LONG).show()
//            }
//        }
    }


    private fun postLogin(email: String, pass: String){
        showLoading(true)
        val bodyLoginRequest = LoginRequest(email, pass)
        val client = ApiConfig.getApiService().postLogin(bodyLoginRequest)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                showLoading(false)
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null){
                    makeToast("message: ${responseBody.message} \n name: ${responseBody.email}")
                    val intent: Intent
                    if (mUserPreference.isUserLoggedIn()){
                        user = mUserPreference.getUser()
                        user.token = responseBody.token
                        mUserPreference.setUser(user)
                        intent = Intent(this@MainActivity, HomeActivity::class.java)
                    }
                    else {
                        user.email = email
                        user.password = pass
                        user.usename = responseBody.username
                        user.token = responseBody.token
                        mUserPreference.setUser(user)
                        intent = Intent(this@MainActivity, PreferenceActivity::class.java)
                    }
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
                else {
                    makeToast(response.message())
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                showLoading(false)
                t.message?.let { makeToast(it) }
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.piLogin.visibility = View.VISIBLE
        } else {
            binding.piLogin.visibility = View.GONE
        }
    }

    private fun makeToast(message : String){
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}