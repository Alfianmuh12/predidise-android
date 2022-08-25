package com.aplikasi.capstone.search

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.aplikasi.capstone.*
import com.aplikasi.capstone.adapter.ListSearchAdapter
import com.aplikasi.capstone.adapter.ListTourismAdapter
import com.aplikasi.capstone.databinding.ActivitySearchBinding
import com.aplikasi.capstone.databinding.ListTourismBinding
import com.aplikasi.capstone.favorite.FavoriteActivity
import com.aplikasi.capstone.home.HomeActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@ExperimentalPagingApi
class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var bindingRv: ListTourismBinding
    private lateinit var mainViewModel: SharedViewModel
    private lateinit var adapter: ListSearchAdapter

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        bindingRv = ListTourismBinding.inflate(layoutInflater)

        setContentView(binding.root)

        adapter = ListSearchAdapter()
        adapter.notifyDataSetChanged()
        adapter.setOnItemClickCallback(object : ListSearchAdapter.OnItemClickCallback{
            override fun onItemClicked(data: ResultItem) {
                Intent(this@SearchActivity, DetailActivity::class.java).also {
                    it.putExtra(DetailActivity.EXTRA_USERNAME, data.name)
                    it.putExtra(DetailActivity.EXTRA_ID, data.id)
                    it.putExtra(DetailActivity.EXTRA_DESCRIPTION, data.description)
                    it.putExtra(DetailActivity.EXTRA_URL, data.thumbnail)
                    it.putExtra(DetailActivity.EXTRA_ADDRESS, data.city)
                    it.putExtra(DetailActivity.EXTRA_PRICE, data.price.toString())
                    it.putExtra(DetailActivity.EXTRA_RATING, data.rating.toString())
                    startActivity(it)
                    finish()
                }
            }
        })

        setupView()
        setupViewModel()
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)


        bottomNavigationView.selectedItemId = R.id.nav_search

        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_search -> return@OnNavigationItemSelectedListener true

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
                R.id.nav_user -> {
                    startActivity(Intent(applicationContext, SettingActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        })

        mainViewModel = ViewModelProvider(this).get(SharedViewModel(UserPreferences.getInstance(dataStore))::class.java)
        binding.apply {
            rvUsers.layoutManager = LinearLayoutManager(this@SearchActivity)
            rvUsers.adapter = adapter
            rvUsers.setHasFixedSize(true)

            searchView.setOnQueryTextListener(object :
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query != null && query.isNotEmpty()) {
                        searchUser()
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })

            mainViewModel.getSearchUsers().observe(this@SearchActivity) {

                if (it != null) {
                    showLoading(false)
                    adapter.setDataUser(it)
                    binding.rvUsers.visibility = View.VISIBLE
                }
            }
        }
    }
    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.INVISIBLE
        }
    }

    private fun searchUser() {
        var token: String
        mainViewModel.getUser().observe(this) {
            if (it.isLogin) {
                token = "Bearer ${it.token}"
                token = "Bearer ${it.token}"
                binding.apply {
                    val query = searchView.query.toString()
                    if (query.isEmpty()) return@observe
                    binding.rvUsers.visibility = View.GONE
                    showLoading(true)
                    mainViewModel.setSearchUser(token, query)

                }
            }
        }
    }


    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferences.getInstance(dataStore))
        )[SharedViewModel::class.java]

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }


    }

}