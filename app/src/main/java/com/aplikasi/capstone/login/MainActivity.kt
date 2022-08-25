package com.aplikasi.capstone.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.paging.ExperimentalPagingApi
import com.aplikasi.capstone.R
import com.aplikasi.capstone.UserModel
import com.aplikasi.capstone.UserPreferences
import com.aplikasi.capstone.ViewModelFactory
import com.aplikasi.capstone.databinding.ActivityMainBinding
import com.aplikasi.capstone.register.Register
import com.aplikasi.capstone.register.RegisterPhoto


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@ExperimentalPagingApi
class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: LoginViewModel
    private lateinit var activityMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
        finishActivity(1)

        supportActionBar?.hide()

        setupViewModel()
        playAnimation()
        setUpAction()
        

        activityMainBinding.toRegister.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

    }
    private fun setUpAction() {
        setMyButtonEnable()
        activityMainBinding.edtEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
        activityMainBinding.edtPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
        activityMainBinding.loginButton.setOnClickListener {
            val email = activityMainBinding.edtEmail.text.toString()
            val password = activityMainBinding.edtPassword.text.toString()
            mainViewModel.loginAccount(email, password)
            mainViewModel.loginResponse.observe(this) {
                if (!it.error) {
                    startActivity(Intent(this, RegisterPhoto::class.java))
                    finish()
                }
            }
        }
        errorMsg()

    }
    private fun errorMsg() {
        mainViewModel.errorCode.observe(this) {
            if (it != -1) {
                when (it) {
                    400 -> {
                        Toast.makeText(
                            this,
                            resources.getString(R.string.error_400),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                    401 -> {
                        Toast.makeText(
                            this,
                            resources.getString(R.string.error_401),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else -> {
                        Toast.makeText(
                            this,
                            "Error: $it",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
    private fun playAnimation(){
        val username = ObjectAnimator.ofFloat(activityMainBinding.edtEmail, View.ALPHA, 1f).setDuration(500)
        val password = ObjectAnimator.ofFloat(activityMainBinding.edtPassword, View.ALPHA, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(activityMainBinding.loginButton, View.ALPHA, 1f).setDuration(500)
        val register = ObjectAnimator.ofFloat(activityMainBinding.toRegister, View.ALPHA, 1f).setDuration(500)

        val together = AnimatorSet().apply {
            playTogether(register, login)
        }
        AnimatorSet().apply {
            playSequentially(username, password, together)
            start()
        }

    }

    private fun setMyButtonEnable() {
        val email = activityMainBinding.edtEmail.text
        val pass = activityMainBinding.edtPassword.text
        activityMainBinding.loginButton.isEnabled =
            email != null && email.toString().isNotEmpty() && pass != null && pass.toString()
                .isNotEmpty()
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferences.getInstance(dataStore))
        )[LoginViewModel::class.java]
        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        mainViewModel.loginResult.observe(this) {
            val account = UserModel(
                id = it.id,
                email = it.email,
                profile = it.profile,
                username = it.username,
                token = it.token,
                isLogin = true
            )
            setReceivedData(account)
        }
    }
    private fun setReceivedData(account: UserModel) {
        mainViewModel.saveAccount(account)
    }

    private fun showLoading(isLoading: Boolean) {
        activityMainBinding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        private const val TAG = "Main Activity"
    }
}
