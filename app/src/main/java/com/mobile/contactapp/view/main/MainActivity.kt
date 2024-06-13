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
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobile.contactapp.R
import com.mobile.contactapp.data.pref.Contact
import com.mobile.contactapp.data.pref.ContactItem
import com.mobile.contactapp.databinding.ActivityMainBinding
import com.mobile.contactapp.databinding.AddContactBinding
import com.mobile.contactapp.view.ViewModelFactory
import com.mobile.contactapp.view.login.LoginActivity
import kotlinx.coroutines.launch


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
        setupZeroContactCardButtons() // Setup Yes/No Buttons moved here

        // Setup FAB click listener
        binding.fabAddContact.setOnClickListener {
            addContactPopUp()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getSession().observe(this) { user ->
            if (user.isLogin) {
                loadContact(user.token)
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }

    internal fun loadContact(token: String) {
        binding.loadingProgressBar.visibility = View.VISIBLE
        viewModel.getContacts(token)
    }

    private fun setupView() {
        // Adjust window insets and hide action bar
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

        // Observe contacts changes
        viewModel.contacts.observe(this) { contacts ->
            itemAdapter.submitList(contacts)
            binding.loadingProgressBar.visibility = View.GONE

            // Show/hide zero contact card based on contacts list
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

        // Set up add button click listener
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
                try {
                    val phoneNumber = phoneNumberInput.toLong()
                    viewModel.addContact(Contact(kontak = ContactItem(firstName, lastName, email, phoneNumber)))

                    // Dismiss popup after adding contact
                    popupWindow.dismiss()
                } catch (e: NumberFormatException) {
                    AlertDialog.Builder(this).apply {
                        setTitle("Ups!")
                        setMessage("Nomor telepon harus berupa angka")
                        setPositiveButton("Kembali") { _, _ -> }
                        create()
                        show()
                    }
                }
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
        // Setup Yes/No buttons in zero contact card
        val yesButton: Button = findViewById(R.id.btn_yes)
        val noButton: Button = findViewById(R.id.btn_no)

        yesButton.setOnClickListener {
            addContactPopUp()
        }

        noButton.setOnClickListener {
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
