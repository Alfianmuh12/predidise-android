package com.aplikasi.capstone.favorite

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.aplikasi.capstone.*
import com.aplikasi.capstone.database.FavoriteUser
import com.aplikasi.capstone.databinding.ActivityFavoriteBinding
import com.aplikasi.capstone.home.HomeActivity
import com.aplikasi.capstone.search.SearchActivity
import com.google.android.material.bottomnavigation.BottomNavigationView


class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding : ActivityFavoriteBinding
    private lateinit var adapterUser : FavoriteAdapter
    private lateinit var viewModel: FavoriteViewModel

    @OptIn(ExperimentalPagingApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Favorite User"

        adapterUser = FavoriteAdapter()
        adapterUser.notifyDataSetChanged()
        viewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)

        adapterUser.setOnItemClickCallback(object: FavoriteAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Favorite) {
                Intent(this@FavoriteActivity, DetailActivity::class.java).also {
                    it.putExtra(DetailActivity.EXTRA_USERNAME, data.username)
                    it.putExtra(DetailActivity.EXTRA_ID, data.id)
                    it.putExtra(DetailActivity.EXTRA_URL, data.avatar_url)
                    it.putExtra(DetailActivity.EXTRA_DESCRIPTION, data.description)
                    it.putExtra(DetailActivity.EXTRA_ADDRESS, data.location)
                    it.putExtra(DetailActivity.EXTRA_PRICE, data.price.toString())
                    it.putExtra(DetailActivity.EXTRA_RATING, data.rating.toString())
                    startActivity(it)
                }
            }
        })

        binding.apply {
            rvAlltourism.setHasFixedSize(true)
            rvAlltourism.layoutManager = LinearLayoutManager(this@FavoriteActivity)
            rvAlltourism.adapter = adapterUser
        }

        viewModel.getFavoriteUser()?.observe(this) {
            if (it != null) {
                val list = mapList(it)
                adapterUser.setDataUser(list)
            }
        }
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)

        // Set Home selected
        bottomNavigationView.selectedItemId = R.id.nav_favorite

        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_favorite -> return@OnNavigationItemSelectedListener true

                R.id.nav_search -> {
                    startActivity(Intent(applicationContext, SearchActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }

                R.id.nav_home -> {
                    startActivity(Intent(applicationContext, HomeActivity::class.java))
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
        binding.rvAlltourism.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)

    }

    private fun mapList(users: List<FavoriteUser>): ArrayList<Favorite> {
        val listUsers = ArrayList<Favorite>()
        for (user in users) {
            val userMapped = user.id?.let {
                Favorite(
                    user.username,
                    it,
                    user.avatar_url,
                    user.location,
                    user.description,
                    user.price,
                    user.rating
                )
            }
            if (userMapped != null) {
                listUsers.add(userMapped)
            }
        }
        return listUsers
    }
}