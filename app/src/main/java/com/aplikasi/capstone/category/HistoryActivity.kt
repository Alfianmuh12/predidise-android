package com.aplikasi.capstone.category

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
import com.aplikasi.capstone.adapter.ListNature
import com.aplikasi.capstone.databinding.ActivityHistoryBinding


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var adapter: ListNature
    private lateinit var viewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ListNature()
        adapter.notifyDataSetChanged()
        adapter.setOnItemClickCallback(object : ListNature.OnItemClickCallback {
            override fun onItemClicked(data: ResultItem) {
                Intent(this@HistoryActivity, DetailActivity::class.java).also {
                    it.putExtra(DetailActivity.EXTRA_USERNAME, data.name)
                    it.putExtra(DetailActivity.EXTRA_ID, data.id)
                    it.putExtra(DetailActivity.EXTRA_URL, data.thumbnail)
                    it.putExtra(DetailActivity.EXTRA_ADDRESS, data.city)
                    it.putExtra(DetailActivity.EXTRA_PRICE, data.price.toString())
                    it.putExtra(DetailActivity.EXTRA_RATING, data.rating.toString())
                    startActivity(it)
                }
            }
        })
        setupView()
        setupViewModel()
        setupAction()
        viewModel =
            ViewModelProvider(this).get(SharedViewModel(UserPreferences.getInstance(dataStore))::class.java)
        binding.apply {
            rvHistory.layoutManager = LinearLayoutManager(this@HistoryActivity)
            rvHistory.adapter = adapter
            rvHistory.setHasFixedSize(true)

        }
        viewModel.getSearchUsers().observe(this@HistoryActivity) {

            if (it != null) {
                showLoading(false)
                adapter.setDataUser(it)
                binding.rvHistory.visibility = View.VISIBLE
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
        finishActivity(1)
    }

    @OptIn(ExperimentalPagingApi::class)
    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferences.getInstance(dataStore))
        )[SharedViewModel::class.java]
        viewModel.isLoading.observe(this) {
            showLoading(it)
        }


    }

    private fun setupAction() {
        var token: String
        viewModel.getUser().observe(this) {
            if (it.isLogin) {
                token = "Bearer ${it.token}"
                token = "Bearer ${it.token}"

                viewModel.setHistory(token)

            }
        }
    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}