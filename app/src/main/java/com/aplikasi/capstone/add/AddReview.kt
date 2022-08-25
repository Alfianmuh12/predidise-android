package com.aplikasi.capstone.add

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.ExperimentalPagingApi
import com.aplikasi.capstone.*
import com.aplikasi.capstone.databinding.ActivityAddReviewBinding
import com.aplikasi.capstone.home.HomeActivity
import com.aplikasi.capstone.login.MainActivity
import com.bumptech.glide.Glide
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File

class AddReview : AppCompatActivity() {
    private lateinit var binding: ActivityAddReviewBinding
    private lateinit var addStoryViewModel: AddViewModel
    private var getFile: File? = null
    private lateinit var token: String
    private var id: Int? = null

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    resources.getString(R.string.permission_denied),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        token = intent.getStringExtra(EXTRA_TOKEN).toString()
        id = intent.getIntExtra(EXTRA_ID, 0)
        val username = intent.getStringExtra(DetailActivity.EXTRA_USERNAME)
        val image = intent.getStringExtra(DetailActivity.EXTRA_URL)
        val description = intent.getStringExtra(DetailActivity.EXTRA_DESCRIPTION)
        val maps = intent.getStringExtra(DetailActivity.EXTRA_MAPS)
        val price = intent.getStringExtra(DetailActivity.EXTRA_PRICE)
        val location = intent.getStringExtra(DetailActivity.EXTRA_ADDRESS)
        val rating = intent.getStringExtra(DetailActivity.EXTRA_RATING)


        setupView()
        setupViewModel()
        setupAction()
    }

    private fun setupView() {
        setMyButtonEnable()
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
        addStoryViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[AddViewModel::class.java]


        addStoryViewModel.isLoading.observe(this) {
            showLoading(it)
        }

    }


    private fun setupAction() {
        binding.ivIcBack.setOnClickListener {
            val intent = Intent(this, DetailActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnGallery.setOnClickListener { startGallery() }
        binding.uploadButton.setOnClickListener { uploadImage() }
        binding.edtDescStory.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
    }

    private lateinit var currentPhotoPath: String
    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            val result = BitmapFactory.decodeFile(myFile.path)

            getFile = myFile
            binding.previewImageView.setImageBitmap(result)
            setMyButtonEnable()
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@AddReview)
            getFile = myFile
            binding.previewImageView.setImageURI(selectedImg)
            setMyButtonEnable()
        }
    }


    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, resources.getString(R.string.choose_img))
        launcherIntentGallery.launch(chooser)
    }


    @OptIn(ExperimentalPagingApi::class)
    private fun uploadImage() {
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "image",
                file.name,
                requestImageFile)
            val tourism_id = id.toString().toRequestBody("text/plain".toMediaType())
            val rating = binding.ratingBar.rating.toString().toRequestBody("text/plain".toMediaType())
            val description =
                binding.edtDescStory.text.toString().toRequestBody("text/plain".toMediaType())
            addStoryViewModel.addStory(tourism_id,  rating ,description,  imageMultipart, token )
            addStoryViewModel.responseUp.observe(this) {
                if (!it.error) {
                    Toast.makeText(
                        this,
                        resources.getString(R.string.up_success),
                        Toast.LENGTH_SHORT
                    ).show()
                    finishAndRemoveTask()
                    onBackPressed()
                } else{
                    Toast.makeText(
                        this,
                        resources.getString(R.string.upload_fail),
                        Toast.LENGTH_SHORT

                    ).show()
                    val intent = Intent(this, DetailActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                }
            }
            errorMsg()

        } else {
            Toast.makeText(
                this@AddReview,
                resources.getString(R.string.empty_img),
                Toast.LENGTH_SHORT
            ).show()
            finishActivity(1)
            onBackPressed()

        }
    }

    private fun errorMsg() {
        addStoryViewModel.responseCode.observe(this) {
            if (it != -1) {
                val codeError = it
                addStoryViewModel.responseMsg.observe(this) { msg ->
                    Toast.makeText(this, "Error $codeError: $msg", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setMyButtonEnable() {
        val desc = binding.edtDescStory.text
        binding.uploadButton.isEnabled =
            desc != null && desc.toString().isNotEmpty() && getFile != null
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_TOKEN = "extra_token"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_URL = "extra_image"
        const val EXTRA_MAPS = "extra_maps"
        const val EXTRA_ADDRESS = "extra_city"
        const val EXTRA_DESCRIPTION = "extra_description"
        const val EXTRA_RATING = "extra_rating"
        const val EXTRA_PRICE = "extra_price"
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}