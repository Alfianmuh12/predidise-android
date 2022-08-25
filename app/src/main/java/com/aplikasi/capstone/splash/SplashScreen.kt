package com.aplikasi.capstone.splash

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Window
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.paging.ExperimentalPagingApi
import com.aplikasi.capstone.*
import com.aplikasi.capstone.home.HomeActivity
import com.aplikasi.capstone.login.MainActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "settings"
)

@ExperimentalPagingApi
class SplashScreen : AppCompatActivity() {
    private lateinit var mainViewModel: SharedViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed({
            setupViewModel()
            supportFinishAfterTransition()
        }, 3000)
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferences.getInstance(dataStore))
        )[SharedViewModel::class.java]

        mainViewModel.getUser().observe(this){user ->
            if (!user.isLogin) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                startActivity(Intent(this, HomeActivity::class.java))
            }
        }
    }
}