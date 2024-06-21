package com.dicoding.kelana.ui.detail.wisata

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.kelana.R
import com.dicoding.kelana.data.DataWisata
import com.dicoding.kelana.data.UserModel
import com.dicoding.kelana.data.response.DetailWisataResponse
import com.dicoding.kelana.databinding.ActivityDetailWisataBinding
import com.dicoding.kelana.db.UserPreference

class DetailWisataActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailWisataBinding
    private lateinit var mUserPreference: UserPreference
    private var user = UserModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailWisataBinding.inflate(layoutInflater)

        supportActionBar?.hide()

        val placeId = intent.getStringExtra("PLACE_ID")

//        Toast.makeText(this@DetailWisataActivity, "id: $placeId", Toast.LENGTH_SHORT).show()

        val wisataViewModel = ViewModelProvider(this)[DetailWisataViewModel::class.java]
        wisataViewModel.wisata.observe(this){ wisata ->
            setData(wisata)
        }

        mUserPreference = UserPreference(this)
        user = mUserPreference.getUser()

//        wisataViewModel.getDataWisata(user.token, placeId)
        user.token?.let { token -> placeId?.let { id -> wisataViewModel.getDataWisata(token, id) } }

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_detail_wisata)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setData(wisata: DetailWisataResponse){
        Glide.with(this)
            .load(wisata.image1)
            .into(binding.ivDetailWisata)
        binding.tvDetailWisataName.text = wisata.placeName
        binding.tvDetailWisataRating.text = wisata.rating.toString()
        binding.tvDetailWisataPref.text = wisata.category
        binding.tvDetailWisataDescription.text = wisata.description
    }
}