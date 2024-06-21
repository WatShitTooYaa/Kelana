package com.dicoding.kelana.preferences

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.kelana.HomeActivity
import com.dicoding.kelana.R
import com.dicoding.kelana.data.DataPreferences
import com.dicoding.kelana.data.UserModel
import com.dicoding.kelana.databinding.ActivityPreferenceBinding
import com.dicoding.kelana.db.UserPreference
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent


class PreferenceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPreferenceBinding
    private val adapter = PreferenceAdapter()
    private lateinit var mUserPreference: UserPreference
    private var user = UserModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityPreferenceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val preferenceViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[PreferenceViewModel::class.java]

        preferenceViewModel.preferences.observe(this){ pref ->
            setPreference(pref)
        }

        mUserPreference = UserPreference(this)


        val layoutManager = GridLayoutManager(this, 2)
//        val layoutManager = CenterGridLayoutManager(this, 2, RecyclerView.HORIZONTAL, false)
        binding.rvActivityPreference.layoutManager = layoutManager
//        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
//        binding.rvActivityPreference.addItemDecoration(CenteredItemDecoration(this))



        binding.btnNext.setOnClickListener {
//            val submitList = adapter.getSelectedItems()
//            Toast.makeText(this@PreferenceActivity, submitList.toString(), Toast.LENGTH_LONG).show()
            val intent = Intent(this@PreferenceActivity, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
            showSelectedPreference()
        }

        adapter.setOnSelectedItemsChangedListener {
            updateButtonState()
        }

        updateButtonState()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun updateButtonState() {
        binding.btnNext.isEnabled = adapter.getSelectedItems().isNotEmpty()
    }

    private fun setPreference(pref: List<DataPreferences>){
        adapter.submitList(pref)
        binding.rvActivityPreference.adapter = adapter
    }

    private fun showSelectedPreference() {
        val selectedItems = adapter.getSelectedItems()
        if (selectedItems.isNotEmpty()) {
            val selectedPreference = selectedItems[0]
            user = mUserPreference.getUser()
            user.preference = selectedPreference.title
            mUserPreference.setUser(user)
            Toast.makeText(this, "user : ${user.usename} \n Selected: ${user.preference}", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "No preference selected", Toast.LENGTH_SHORT).show()
        }
    }
}