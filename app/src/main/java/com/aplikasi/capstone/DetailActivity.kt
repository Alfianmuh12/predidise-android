package com.aplikasi.capstone

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.aplikasi.capstone.adapter.ListReview
import com.aplikasi.capstone.adapter.ListTourismAdapter
import com.aplikasi.capstone.add.AddReview
import com.aplikasi.capstone.databinding.ActivityDetailBinding
import com.aplikasi.capstone.databinding.ListReviewBinding
import com.aplikasi.capstone.favorite.FavoriteViewModel
import com.aplikasi.capstone.response.Review
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")


class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: SharedViewModel
    private lateinit var mainViewModel: FavoriteViewModel
    private lateinit var bindingRv: ListReviewBinding
    private lateinit var adapter: ListReview

    @OptIn(ExperimentalPagingApi::class)
    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bindingRv = ListReviewBinding.inflate(layoutInflater)
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.recyclerView.addItemDecoration(itemDecoration)

        setupView()
        adapter = ListReview()
        adapter.notifyDataSetChanged()

        val username = intent.getStringExtra(EXTRA_USERNAME)
        val id = intent.getIntExtra(EXTRA_ID,0)
        val image = intent.getStringExtra(EXTRA_URL)
        val description = intent.getStringExtra(EXTRA_DESCRIPTION)
        val maps = intent.getStringExtra(EXTRA_MAPS)
        val price = intent.getStringExtra(EXTRA_PRICE)
        val location = intent.getStringExtra(EXTRA_ADDRESS)
        val rating = intent.getStringExtra(EXTRA_RATING)

        mainViewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)

        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferences.getInstance(dataStore))
        )[SharedViewModel::class.java]


        var token: String
        viewModel.getUser().observe(this) {
            if (it.isLogin) {
                token = "Bearer ${it.token}"
                binding.btnAdd.setOnClickListener {
                    val i = Intent(this@DetailActivity, AddReview::class.java)
                    i.putExtra(AddReview.EXTRA_TOKEN, token)
                    i.putExtra(AddReview.EXTRA_ID, id)
                    i.putExtra(AddReview.EXTRA_URL, image)
                    i.putExtra(AddReview.EXTRA_MAPS, maps)
                    i.putExtra(AddReview.EXTRA_DESCRIPTION, description)
                    i.putExtra(AddReview.EXTRA_ADDRESS, location)
                    i.putExtra(AddReview.EXTRA_PRICE, price.toString())
                    i.putExtra(AddReview.EXTRA_RATING, rating.toString())
                    startActivity(i)
                }
                viewModel.setReview(id.toString(),token)
            }
        }

        binding.nameLocation.text = username
        binding.tvDescription.text = description
        Glide.with(this).load(image).into(binding.imageView3)
        binding.addressLocation.text = location
        binding.priceLocation.text =  "Rp"+ price.toString()
        binding.rattingLocation.text = rating?.toDouble().toString()

        binding.btnMaps.setOnClickListener {
            startActivity( Intent(Intent.ACTION_VIEW, Uri.parse(maps)))
            finish()
        }

        binding.ivBack.setOnClickListener {
            finishAndRemoveTask()
            onBackPressed()
        }

        showLoading(false)

        var _isChecked = false
        CoroutineScope(Dispatchers.IO).launch {
            val count =  mainViewModel.checkUser(id)
            withContext(Dispatchers.Main){
                if (count != null){
                    if(count>0){
                        binding.toggleFav.isChecked = true
                        _isChecked = true
                    } else {
                        binding.toggleFav.isChecked = false
                        _isChecked = false
                    }
                }
            }
        }

        binding.toggleFav.setOnClickListener {

            _isChecked = !_isChecked
            if (_isChecked && username != null && image != null && description != null && location != null && price != null && rating != null) {
                mainViewModel.addToFavorite(username, image,description, id, location, price, rating)
            }
            else {
                mainViewModel.deleteFromFavorite(id)
            }
            binding.toggleFav.isChecked = _isChecked
        }

        viewModel =
            ViewModelProvider(this).get(SharedViewModel(UserPreferences.getInstance(dataStore))::class.java)
        binding.apply {
            recyclerView.layoutManager = LinearLayoutManager(this@DetailActivity)
            recyclerView.adapter = adapter
            recyclerView.setHasFixedSize(true)

        }
        viewModel.getReview().observe(this@DetailActivity) {

            if (it != null) {
                showLoading(false)
                adapter.setDataUser(it)
                binding.recyclerView.visibility = View.VISIBLE
            }
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)

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

    private fun showLoading(isLoading: Boolean) {
        binding.progressBarDetail.visibility= if(isLoading) View.VISIBLE else View.GONE
    }


    companion object {
        const val EXTRA_USERNAME = "extra_name"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_URL = "extra_image"
        const val EXTRA_MAPS = "extra_maps"
        const val EXTRA_ADDRESS = "extra_city"
        const val EXTRA_DESCRIPTION = "extra_description"
        const val EXTRA_RATING = "extra_rating"
        const val EXTRA_PRICE = "extra_price"


    }
}