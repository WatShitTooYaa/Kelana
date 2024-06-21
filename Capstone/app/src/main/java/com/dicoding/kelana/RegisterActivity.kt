package com.dicoding.kelana

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.kelana.data.api.ApiConfig
import com.dicoding.kelana.data.api.RegisterRequest
import com.dicoding.kelana.data.response.RegisterResponse
import com.dicoding.kelana.databinding.ActivityRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        supportActionBar?.hide()

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val pass = binding.etPassword.text.toString()
            val user = binding.etUsername.text.toString()
            if (email.isNotEmpty() && pass.isNotEmpty() && user.isNotEmpty()){
                postRegister(email, pass, user)
            }
            else {
                makeToast("masih kosong")
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun postRegister(email: String, pass: String, user: String){
        showLoading(true)

        val registerRequest = RegisterRequest(email, pass, user)
        val client = ApiConfig.getApiService().postRegister(registerRequest)
        client.enqueue(object : Callback<RegisterResponse>{
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                showLoading(false)
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null){
                    makeToast("message: ${responseBody.message} \n id: ${responseBody.userId}")
                    finish()
                }
                else {
                    makeToast(response.message())
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                showLoading(false)
                t.message?.let { makeToast(it) }
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.piRegister.visibility = View.VISIBLE
        } else {
            binding.piRegister.visibility = View.GONE
        }
    }

    private fun makeToast(message : String){
        Toast.makeText(this@RegisterActivity, message, Toast.LENGTH_SHORT).show()
    }

}