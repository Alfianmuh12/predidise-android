package com.aplikasi.capstone.home

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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.aplikasi.capstone.*
import com.aplikasi.capstone.adapter.ListRecommendation
import com.aplikasi.capstone.adapter.ListTourismAdapter
import com.aplikasi.capstone.category.HistoryActivity
import com.aplikasi.capstone.category.NatureActivity
import com.aplikasi.capstone.databinding.ActivityAddReviewBinding
import com.aplikasi.capstone.databinding.ActivityHomeBinding
import com.aplikasi.capstone.databinding.ItemReccomendationTourismBinding
import com.aplikasi.capstone.favorite.FavoriteActivity
import com.aplikasi.capstone.search.SearchActivity
import com.bumptech.glide.Glide

import com.google.android.material.bottomnavigation.BottomNavigationView


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")


@ExperimentalPagingApi
class HomeActivity : AppCompatActivity() {
    //private val homeViewModel: HomeViewModel by viewModels { HomeViewModel.ViewModelFactory(this) }
    private lateinit var activityHomeBinding: ActivityHomeBinding
    private lateinit var itemRecomendation: ItemReccomendationTourismBinding
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var adapter: ListRecommendation
    private lateinit var binding: ActivityAddReviewBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        activityHomeBinding = ActivityHomeBinding.inflate(layoutInflater)
        itemRecomendation = ItemReccomendationTourismBinding.inflate(layoutInflater)
        val layoutManager = LinearLayoutManager(this)
        activityHomeBinding.rvReccomendTourism.layoutManager = layoutManager
        val decoration = DividerItemDecoration(this, layoutManager.orientation)
        activityHomeBinding.rvReccomendTourism.addItemDecoration(decoration)
        super.onCreate(savedInstanceState)
        activityHomeBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(activityHomeBinding.root)
        setupViewModel()
        setupView()
        setupAction()


        supportActionBar?.hide()
        adapter = ListRecommendation()
        adapter.notifyDataSetChanged()
        adapter.setOnItemClickCallback(object : ListRecommendation.OnItemClickCallback {
            override fun onItemClicked(data: ResultItem) {
                Intent(this@HomeActivity, DetailActivity::class.java).also {
                    it.putExtra(DetailActivity.EXTRA_USERNAME, data.name)
                    it.putExtra(DetailActivity.EXTRA_ID, data.id)
                    it.putExtra(DetailActivity.EXTRA_URL, data.thumbnail)
                    it.putExtra(DetailActivity.EXTRA_MAPS, data.maps)
                    it.putExtra(DetailActivity.EXTRA_DESCRIPTION, data.description)
                    it.putExtra(DetailActivity.EXTRA_ADDRESS, data.city)
                    it.putExtra(DetailActivity.EXTRA_PRICE, data.price.toString())
                    it.putExtra(DetailActivity.EXTRA_RATING, data.rating.toString())
                    startActivity(it)
                }
            }
        })


        // Initialize and assign variable
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)

        // Set Home selected
        bottomNavigationView.selectedItemId = R.id.nav_home

        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> return@OnNavigationItemSelectedListener true

                R.id.nav_search -> {
                    startActivity(Intent(applicationContext, SearchActivity::class.java))
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
        sharedViewModel =
            ViewModelProvider(this).get(SharedViewModel(UserPreferences.getInstance(dataStore))::class.java)
        activityHomeBinding.apply {
            rvReccomendTourism.layoutManager = LinearLayoutManager(this@HomeActivity)
            rvReccomendTourism.adapter = adapter
            rvReccomendTourism.setHasFixedSize(true)

        }
        sharedViewModel.getSearchUsers().observe(this@HomeActivity) {

            if (it != null) {
                showLoading(false)
                adapter.setDataUser(it)
                activityHomeBinding.rvReccomendTourism.visibility = View.VISIBLE
            }
        }
        activityHomeBinding.rvReccomendTourism.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)

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
        activityHomeBinding.cvNature.setOnClickListener {
            val intent = Intent(this, NatureActivity::class.java)
            startActivity(intent)
            finish()

        }
        activityHomeBinding.cvHistory.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
            finish()

        }

        supportActionBar?.hide()
    }

    private fun setupAction() {
        var token: String
        sharedViewModel.getUser().observe(this) {
            if (it.isLogin) {
                token = "Bearer ${it.token}"
                token = "Bearer ${it.token}"

                sharedViewModel.setUserDetail(token)
                sharedViewModel.setRecomendation(token)
                sharedViewModel.getUser().observe(this) {
                    Glide.with(this)
                        .load(it.profile)
                        .into(activityHomeBinding.ivProfile)

                    activityHomeBinding.tvUsername.text = it.username
                }
                val i = Intent(this@HomeActivity, DetailActivity::class.java)
                startActivity(i)

            }
        }
    }
    private fun setupViewModel() {
        sharedViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferences.getInstance(dataStore))
        )[SharedViewModel::class.java]
        sharedViewModel.list.observe(this) {
            setTourismData(it)
        }
        sharedViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }


    private fun setTourismData(stories: List<ResultItem>) {
        activityHomeBinding.rvReccomendTourism.layoutManager = LinearLayoutManager(this)
        val storyAdapter = ListTourismAdapter(stories)
        activityHomeBinding.rvReccomendTourism.adapter = storyAdapter
        storyAdapter.setOnItemClickCallback(object : ListTourismAdapter.OnItemClickCallback {
            override fun onItemClicked(
                accountClicked: ResultItem
            ) {
                val i = Intent(this@HomeActivity, DetailActivity::class.java)
                startActivity(i)
            }
        })
    }



    private fun showLoading(isLoading: Boolean) {
        activityHomeBinding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

//    private fun getStories() {
//        val adapter = ListTourismAdapter()
//        activityHomeBinding.rvReccomendTourism.adapter = adapter.withLoadStateFooter(
//            footer = LoadingStateAdapter {
//                adapter.retry()
//            }
//        )
//        sharedViewModel.getUser().observe(this) { userAuth ->
//            if (userAuth != null) {
//                homeViewModel.tourism("Bearer " + userAuth.token).observe(this) { stories ->
//                    adapter.submitData(lifecycle, stories)
//                }
//            }
//        }
//    }
//
//    private fun setRecyclerView() {
//        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            activityHomeBinding.rvReccomendTourism.layoutManager = GridLayoutManager(this, 2)
//        } else {
//            activityHomeBinding.rvReccomendTourism.layoutManager = LinearLayoutManager(this)
//        }
//        adapter = ListTourismAdapter()
//        activityHomeBinding.rvReccomendTourism.adapter = adapter.withLoadStateFooter(footer = LoadingStateAdapter {
//            adapter.retry()
//        })
//    }
//
//    private fun setupViewModel() {
//        sharedViewModel = ViewModelProvider(
//            this,
//            ViewModelFactory(UserPreferences.getInstance(dataStore))
//        )[SharedViewModel::class.java]
//        sharedViewModel.getUser().observe(this) {story ->
//            this.paging = story
//            if (story.isLogin) {
//                sharedViewModel.pagingStory(story.token).observe(this) {
//                    adapter.setListStory(it)
//                    adapter.submitData(lifecycle, it)
//                }
//            }
//        }
//    }




}