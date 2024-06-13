package com.mobile.contactapp.view.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import android.widget.PopupWindow
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobile.contactapp.R
import com.mobile.contactapp.data.pref.Contact
import com.mobile.contactapp.data.pref.ContactItem
import com.mobile.contactapp.databinding.ActivityMainBinding
import com.mobile.contactapp.databinding.AddContactBinding
import com.mobile.contactapp.view.ViewModelFactory
import com.mobile.contactapp.view.login.LoginActivity


class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding
    private lateinit var itemAdapter: ItemAdapter
    private lateinit var popupWindow: PopupWindow

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupRecyclerView()
        setupViewModel()
        setupAction()
        setupZeroContactCardButtons()

        binding.fabAddContact.setOnClickListener {
            addContactPopUp()
        }
    }

    fun loadContact(token: String) {
        binding.loadingProgressBar.visibility = View.VISIBLE
        viewModel.getContacts(token)
    }

    @Suppress("DEPRECATION")
    private fun setupView() {
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

    private fun setupRecyclerView() {
        // Setup RecyclerView
        itemAdapter = ItemAdapter(viewModel, this, this)
        binding.rvStory.layoutManager = LinearLayoutManager(this)
        binding.rvStory.adapter = itemAdapter
    }

    private fun setupViewModel() {
        // Observe session changes
        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                loadContact(user.token)
            }
        }

        viewModel.contacts.observe(this) { contacts ->
            itemAdapter.submitList(contacts)
            binding.loadingProgressBar.visibility = View.GONE

            if (contacts.isEmpty()) {
                binding.zeroContactCard.visibility = View.VISIBLE
            } else {
                binding.zeroContactCard.visibility = View.GONE
            }
        }

        // Observe errors
        viewModel.error.observe(this) { error ->
            if (error == "Unauthorized") {
                showUnauthorizedDialog()
            } else {
                showToast(error)
                binding.loadingProgressBar.visibility = View.GONE
            }
        }
    }

    private fun setupAction() {
        // Setup action logout click listener
        binding.actionLogout.setOnClickListener {
            viewModel.logout()
        }
    }

    private fun addContactPopUp() {
        // Hide zero contact card
        binding.zeroContactCard.visibility = View.GONE

        // Inflate and show popup window for adding contact
        val popupBinding = AddContactBinding.inflate(layoutInflater)
        popupWindow = PopupWindow(
            popupBinding.root,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            true
        )

        popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.rounded_corner))
        popupWindow.showAtLocation(binding.root, Gravity.CENTER, 0, 0)

        val emailEditText = popupBinding.edAddEmail
        val emailInputLayout = popupBinding.emailEditTextLayout
        emailInputLayout.setEditText(emailEditText)

        popupBinding.addBtn.setOnClickListener {
            val firstName = popupBinding.edAddFirstName.text.toString()
            val lastName = popupBinding.edAddLastName.text.toString()
            val email = popupBinding.edAddEmail.text.toString()
            val phoneNumberInput = popupBinding.edAddPhoneNumber.text.toString()

            if (firstName.isBlank() || email.isBlank() || phoneNumberInput.isBlank()) {
                AlertDialog.Builder(this).apply {
                    val notFilled = when {
                        firstName.isBlank() -> "Nama depan"
                        phoneNumberInput.isBlank() -> "Nomor telepon"
                        else -> "Email"
                    }

                    setTitle("Ups!")
                    setMessage(getString(R.string.not_filled_required, notFilled))
                    setPositiveButton("Kembali") { _, _ -> }
                    create()
                    show()
                }
            } else {
                val phoneNumber = phoneNumberInput.toLong()
                viewModel.addContact(Contact(kontak = ContactItem(firstName, lastName, email, phoneNumber)))
                viewModel.success.observe(this) { isSuccess ->
                    if (isSuccess) {
                        Toast.makeText(this,
                            getString(R.string.add_contact_success), Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this,
                            getString(R.string.add_contact_failed), Toast.LENGTH_SHORT).show()
                    }
                }
                popupWindow.dismiss()
            }
        }

        // Set dismiss listener to show zero contact card again if needed
        popupWindow.setOnDismissListener {
            if (viewModel.contacts.value?.isEmpty() == true) {
                binding.zeroContactCard.visibility = View.VISIBLE
            }
        }
    }


    private fun setupZeroContactCardButtons() {

        binding.btnYes.setOnClickListener {
            addContactPopUp()
        }

        binding.btnNo.setOnClickListener {
            binding.zeroContactCard.visibility = View.GONE
        }
    }

    private fun showUnauthorizedDialog() {
        // Show unauthorized dialog and handle "Log In" action
        AlertDialog.Builder(this).apply {
            setTitle("Unauthorized")
            setMessage("Session has expired. Please log in again.")
            setPositiveButton("Log In") { _, _ ->
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            }
            create()
            show()
        }
    }

    private fun showToast(message: String) {
        // Show toast message
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
