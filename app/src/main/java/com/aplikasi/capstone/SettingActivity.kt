package com.aplikasi.capstone

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.paging.ExperimentalPagingApi
import com.aplikasi.capstone.add.AddPlaceActivity
import com.aplikasi.capstone.databinding.ActivitySettingBinding
import com.aplikasi.capstone.favorite.FavoriteActivity
import com.aplikasi.capstone.home.HomeActivity
import com.aplikasi.capstone.login.MainActivity
import com.aplikasi.capstone.register.RegisterPhoto
import com.aplikasi.capstone.search.SearchActivity
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")


class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding
    private lateinit var mainViewModel: SharedViewModel
    @OptIn(ExperimentalPagingApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)


        bottomNavigationView.selectedItemId = R.id.nav_user

        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_user -> return@OnNavigationItemSelectedListener true

                R.id.nav_home ->{
                    startActivity(Intent(applicationContext, HomeActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }

                R.id.nav_favorite -> {
                    startActivity(Intent(applicationContext, FavoriteActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_search -> {
                    startActivity(Intent(applicationContext, SearchActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        })

        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferences.getInstance(dataStore))
        )[SharedViewModel::class.java]

        var token: String
        mainViewModel.getUser().observe(this) {
            if (it.isLogin) {
                token = "Bearer ${it.token}"
                mainViewModel.getStory(token)
                mainViewModel.getUser().observe(this) {
                    Glide.with(this)
                        .load(it.profile.toString())
                        .into(binding.ivProfile)

                    binding.tvUsername.text = it.username
                    binding.tvEmail.text = it.email
                }
                binding.cvEdit.setOnClickListener {
                    val i = Intent(this, RegisterPhoto::class.java)
                    startActivity(i)
                    finish()
                }
                binding.cvCreateTourism.setOnClickListener {
                    val i = Intent(this, AddPlaceActivity::class.java)
                    i.putExtra(AddPlaceActivity.EXTRA_TOKEN, token)
                    startActivity(i)
                    finish()
                }
                binding.cvLogout.setOnClickListener {
                    mainViewModel.logout()
                    finishActivity(1)
                }
                binding.cvChangeLanguage.setOnClickListener {
                    val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                    startActivity(intent)

                }

            } else {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }
}