package com.aplikasi.capstone.register

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
import com.aplikasi.capstone.add.AddPlaceActivity
import com.aplikasi.capstone.api.ApiConfig
import com.aplikasi.capstone.databinding.ActivityRegisterPhotoBinding
import com.aplikasi.capstone.home.HomeActivity
import com.aplikasi.capstone.response.ProfileResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.io.File

@ExperimentalPagingApi
class RegisterPhoto : AppCompatActivity() {

    private lateinit var addStoryViewModel: SharedViewModel
    private lateinit var binding: ActivityRegisterPhotoBinding
    private var getFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setupViewModel()

        binding.addPhoto.setOnClickListener {
            startGallery()
        }

        binding.upload.setOnClickListener {
            uploadImage()
        }
        binding.loginButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupViewModel() {
        addStoryViewModel = ViewModelProvider(
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
            val file = reduceFileImage(getFile as File)
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "image",
                file.name,
                requestImageFile
            )

            addStoryViewModel.getUser().observe(this) {
                if (it != null) {
                    val client = ApiConfig.getApiService()
                        .uploadProfile("Bearer " + it.token, imageMultipart)
                    client.enqueue(object : Callback<ProfileResponse> {
                        override fun onResponse(
                            call: Call<ProfileResponse>,
                            response: retrofit2.Response<ProfileResponse>
                        ) {
                            showLoading(false)
                            val responseBody = response.body()
                            Log.d(TAG, "onResponse: $responseBody")
                            if (response.isSuccessful && responseBody?.message == "success") {
                                Toast.makeText(
                                    this@RegisterPhoto,
                                    getString(R.string.upload_success),
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent = Intent(this@RegisterPhoto, HomeActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Log.e(TAG, "onFailure1: ${response.message()}")
                                Toast.makeText(
                                    this@RegisterPhoto,
                                    getString(R.string.upload_fail),
                                    Toast.LENGTH_SHORT
                                ).show()
                                val jObjError = JSONObject(response.errorBody()!!.string())
                                Log.e(AddPlaceActivity.TAG, "NJAY: ${jObjError.toString()}")
                            }
                        }

                        override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                            showLoading(false)
                            Log.e(TAG, "onFailure2: ${t.message}")
                            Toast.makeText(
                                this@RegisterPhoto,
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
            val myFile = uriToFile(selectedImg, this@RegisterPhoto)
            getFile = myFile
            binding.imageView.setImageURI(selectedImg)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val TAG = "AddStoryActivity"

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}