package com.dicoding.storyapp.story

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.storyapp.MainActivity
import com.dicoding.storyapp.R
import com.dicoding.storyapp.UserModel
import com.dicoding.storyapp.UserPreference
import com.dicoding.storyapp.data.response.GetStoryResponse
import com.dicoding.storyapp.data.response.ListStoryItem
import com.dicoding.storyapp.data.retrofit.ApiConfig
import com.dicoding.storyapp.databinding.ActivityStoryBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryBinding
    private lateinit var mUserPreference: UserPreference
    private lateinit var storyViewModel: StoryViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mUserPreference = UserPreference(this)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                AlertDialog.Builder(this@StoryActivity)
                    .setMessage("Apakah anda ingin keluar")
                    .setPositiveButton("Yes") { dialog, which ->
                        finishAffinity()
                    }
                    .setNegativeButton("No", null)
                    .show()
            }
        })

        val factory = StoryViewModelFactory(this)
        storyViewModel = ViewModelProvider(this, factory)[StoryViewModel::class.java]

        storyViewModel.listStory.observe(this) {story ->
            setStoryData(story)
        }

        storyViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvStories.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvStories.addItemDecoration(itemDecoration)


        val fragmentContainer = binding.fragmentContainer
        val fadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount == 0) {
                // Menunggu animasi selesai sebelum mengubah visibilitas
                fragmentContainer.postDelayed({
                    fragmentContainer.startAnimation(fadeOutAnimation)
                    fragmentContainer.visibility = View.GONE
                }, resources.getInteger(android.R.integer.config_shortAnimTime).toLong())
            }
        }

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId) {
                R.id.menu_addStory -> {
                    val intent = Intent(this@StoryActivity, AddStoryActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.menu_logout -> {
                    Toast.makeText(this@StoryActivity, "logout", Toast.LENGTH_SHORT).show()
                    mUserPreference.deleteUser()
                    if (mUserPreference.preference.all.isEmpty()) {
//                        Log.d("Preferences", "Semua data berhasil dihapus.")
                        val intent = Intent(this@StoryActivity, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        Toast.makeText(this@StoryActivity, "Berhasil keluar", Toast.LENGTH_SHORT).show()
                        startActivity(intent)
                        finish()
                    } else {
//                        Log.d("Preferences", "Gagal menghapus semua data.")
                        Toast.makeText(this@StoryActivity, "Gagal keluar", Toast.LENGTH_SHORT).show()
                    }
                    true
                }
                else -> false
            }
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    override fun onResume() {
        super.onResume()
        storyViewModel.refreshStory()
    }

    private fun setStoryData(story: List<ListStoryItem>) {
        val adapter = ListStoryAdapter(this, supportFragmentManager, R.id.fragment_container)
        adapter.submitList(story)
        binding.rvStories.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}