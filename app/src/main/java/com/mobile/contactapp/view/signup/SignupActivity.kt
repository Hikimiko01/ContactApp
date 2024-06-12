package com.mobile.contactapp.view.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.mobile.contactapp.R
import com.mobile.contactapp.databinding.ActivitySignupBinding
import com.mobile.contactapp.view.ViewModelFactory
import com.mobile.contactapp.view.login.LoginActivity

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding

    private val viewModel by viewModels<SignUpViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.isLoading.observe(this) {isLoading -> binding.loadingProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE }
        setupView()
        setupAction()
        playAnimation()
        setupTextWatchers()
    }

    private fun setupTextWatchers(){

        val emailEditText = binding.edRegisterEmail
        val emailInputLayout = binding.emailEditTextLayout
        emailInputLayout.setEditText(emailEditText)

        val passwordEditText = binding.edRegisterPassword
        val passwordInputLayout = binding.passwordEditTextLayout
        passwordInputLayout.setEditText(passwordEditText)

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

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()

            binding.loadingProgressBar.visibility = View.VISIBLE

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                viewModel.register(name, email, password) { response ->
                    if (!response.error) {
                        // Menghilangkan progress bar ketika menerima respon
                        binding.loadingProgressBar.visibility = View.GONE

                        AlertDialog.Builder(this).apply {
                            setTitle("Yeah!")
                            setMessage("Akun dengan $email sudah jadi nih. Yuk, login dan simpan kontakmu!")
                            setPositiveButton("Lanjut") { _, _ ->
                                val intent = Intent(context, LoginActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                                finish()
                            }
                            create()
                            show()
                        }
                    } else {
                        AlertDialog.Builder(this).apply {
                            setTitle("Ups!")
                            setMessage(getString(R.string.registered_email))
                            setPositiveButton("Kembali") { _, _ -> }
                            create()
                            show()
                        }
                    }
                }
            } else {
                binding.loadingProgressBar.visibility = View.GONE
                AlertDialog.Builder(this).apply {
                    setTitle("Ups!")
                    setMessage(getString(R.string.field_not_filled))
                    setPositiveButton("Kembali") { _, _ -> }
                    create()
                    show()
                }
            }
        }

        binding.clickableLoginTv.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val nameTextView =
            ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(100)
        val nameEditTextLayout =
            ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(100)
        val loginTV = ObjectAnimator.ofFloat(binding.loginTv, View.ALPHA, 1f).setDuration(100)
        val loginBtnTV = ObjectAnimator.ofFloat(binding.clickableLoginTv, View.ALPHA, 1f).setDuration(100)


        AnimatorSet().apply {
            playSequentially(
                title,
                nameTextView,
                nameEditTextLayout,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                signup,
                loginTV,
                loginBtnTV
            )
            startDelay = 100
        }.start()
    }
}