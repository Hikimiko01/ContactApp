package com.mobile.contactapp.view.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.PopupWindow
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupRecyclerView()
        setupViewModel()
        setupAction()
        addContactPopUp()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getSession().observe(this) { user ->
            loadContact(user.token)
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
    }

    private fun setupRecyclerView() {
        itemAdapter = ItemAdapter(viewModel)
        binding.rvStory.layoutManager = LinearLayoutManager(this)
        binding.rvStory.adapter = itemAdapter
    }

    private fun setupViewModel() {
        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                loadContact(user.token)
            }
        }

        viewModel.contacts.observe(this) { contacts ->
            Log.d("MainActivity", "Updating adapter with contacts: $contacts")
            itemAdapter.submitList(contacts)
            binding.loadingProgressBar.visibility = View.GONE
        }
    }

    private fun setupAction() {
        binding.actionLogout.setOnClickListener {
            viewModel.logout()
        }
    }

    private fun loadContact(token: String) {
        binding.loadingProgressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            viewModel.getContacts(token)
        }
    }

    private fun addContactPopUp(){
        binding.fabAddContact.setOnClickListener {

            val popupBinding = AddContactBinding.inflate(layoutInflater)

            val popupWindow = PopupWindow(
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
                val phoneNumber = popupBinding.edAddPhoneNumber.text.toString().toLong()
                val email = popupBinding.edAddEmail.text.toString()

                viewModel.addContact(Contact(kontak = ContactItem(firstName, lastName, email, phoneNumber)))

                popupWindow.dismiss()
                viewModel.getSession().observe(this) { user ->
                    loadContact(user.token)
                }
            }
        }
    }
}