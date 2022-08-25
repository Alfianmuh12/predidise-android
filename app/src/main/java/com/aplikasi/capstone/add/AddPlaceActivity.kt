package com.aplikasi.capstone.add

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.paging.ExperimentalPagingApi
import com.aplikasi.capstone.*
import com.aplikasi.capstone.api.ApiConfig
import com.aplikasi.capstone.databinding.ActivityAddPlaceBinding
import com.aplikasi.capstone.register.dataStore
import com.aplikasi.capstone.response.UploadResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.io.File

class AddPlaceActivity : AppCompatActivity() {

    private lateinit var viewModel: SharedViewModel
    private lateinit var binding: ActivityAddPlaceBinding
    private var getFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPlaceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setupViewModel()

        binding.ivGallery.setOnClickListener {
            startGallery()
        }

        binding.uploadButton.setOnClickListener {
            uploadImage()
        }

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

    }

    @OptIn(ExperimentalPagingApi::class)
    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferences.getInstance(dataStore))
        )[SharedViewModel::class.java]
    }


    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun uploadImage() {
        showLoading(true)

        if (getFile != null) {

            val nama = binding.inputNama.text.toString().toRequestBody("text/plain".toMediaType())
            val description = binding.inputDescription.text.toString().toRequestBody("text/plain".toMediaType())
            val category = binding.inputCategory.text.toString().toRequestBody("text/plain".toMediaType())
            val city = binding.inputCity.text.toString().toRequestBody("text/plain".toMediaType())
            val price = binding.inputPrice.text.toString().toRequestBody("text/plain".toMediaType())
            val rating = binding.ratingBar.rating.toInt().toString().toRequestBody("text/plain".toMediaType())
            val time = 1.toString().toRequestBody("text/plain".toMediaType())
            val coodinate = "1".toRequestBody("text/plain".toMediaType())
            val file = reduceFileImage(getFile as File)
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "thumbnail",
                file.name,
                requestImageFile)

            viewModel.getUser().observe(this) {
                if (it != null) {
                    val client = ApiConfig.getApiService()
                        .createTourism(nama,description, category,city,price,rating, time, coodinate,  imageMultipart,"Bearer " + it.token)
                    client.enqueue(object : Callback<UploadResponse> {
                        override fun onResponse(
                            call: Call<UploadResponse>,
                            response: retrofit2.Response<UploadResponse>
                        ) {
                            showLoading(false)
                            val responseBody = response.body()
                            Log.d(TAG, "onResponse: $responseBody")
                            if (response.isSuccessful && responseBody?.message == "success") {
                                Toast.makeText(
                                    this@AddPlaceActivity,
                                    getString(R.string.upload_success),
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent = Intent(this@AddPlaceActivity, SettingActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Log.e(TAG, "onFailure1: ${response.message()}")
                                val jObjError = JSONObject(response.errorBody()!!.string())
                                Log.e(AddPlaceActivity.TAG, "NJAY: ${jObjError.toString()}")
                                Toast.makeText(
                                    this@AddPlaceActivity,
                                    getString(R.string.upload_fail),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                            showLoading(false)
                            Log.e(TAG, "onFailure2: ${t.message}")
                            Toast.makeText(
                                this@AddPlaceActivity,
                                getString(R.string.upload_fail),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
                }
                showLoading(true)
            }

        }

    }
    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@AddPlaceActivity)
            getFile = myFile
            binding.previewImageView.setImageURI(selectedImg)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


    companion object {
        const val TAG = "AddPlaceActivity"
        const val EXTRA_TOKEN = "extra_token"
    }

}