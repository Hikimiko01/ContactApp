package com.mobile.contactapp.view.main

import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupWindow
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mobile.contactapp.R
import com.mobile.contactapp.data.UserRepository
import com.mobile.contactapp.data.api.response.ListContacts
import com.mobile.contactapp.data.pref.Contact
import com.mobile.contactapp.data.pref.ContactItem
import com.mobile.contactapp.databinding.EditContactBinding
import com.mobile.contactapp.databinding.StoryCardBinding
import kotlinx.coroutines.launch

class ItemAdapter (
    private val viewModel: MainViewModel,
    private val lifecycleOwner: LifecycleOwner,
    private val mainActivity: MainActivity
) : ListAdapter<ListContacts, ItemAdapter.ItemViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = StoryCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ItemViewHolder(private val binding: StoryCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(contact: ListContacts) {
            binding.tvItemName.text = binding.root.context.getString(R.string.contacts_name, contact.firstName, contact.lastName)
            binding.tvItemPhoneNum.text = binding.root.context.getString(R.string.contacts_phoneNum, contact.phoneNumber.toString())
            binding.tvItemEmail.text = binding.root.context.getString(R.string.contacts_email, contact.email)
            binding.contactsEditBtn.setOnClickListener {
                editContactPopUp(contact)
            }
            binding.contactsDeleteBtn.setOnClickListener {
                viewModel.deleteContact(contact.id)
            }
        }

        private fun editContactPopUp(contact: ListContacts) {
            val inflater = LayoutInflater.from(binding.root.context)
            val popupBinding = EditContactBinding.inflate(inflater)

            popupBinding.edAddFirstName.setText(contact.firstName)
            popupBinding.edAddLastName.setText(contact.lastName)
            popupBinding.edAddPhoneNumber.setText(contact.phoneNumber.toString())
            popupBinding.edAddEmail.setText(contact.email)

            val popupWindow = PopupWindow(
                popupBinding.root,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                true
            )

            popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(binding.root.context, R.drawable.rounded_corner
            ))

            popupWindow.showAtLocation(binding.root, Gravity.CENTER, 0, 0)

            val emailEditText = popupBinding.edAddEmail
            val emailInputLayout = popupBinding.emailEditTextLayout
            emailInputLayout.setEditText(emailEditText)

            popupBinding.addBtn.setOnClickListener {
                val firstName = popupBinding.edAddFirstName.text.toString()
                val lastName = popupBinding.edAddLastName.text.toString()
                val phoneNumberInput = popupBinding.edAddPhoneNumber.text.toString()
                val email = popupBinding.edAddEmail.text.toString()

                if (firstName.isBlank() || email.isBlank() || phoneNumberInput.isBlank()) {
                    AlertDialog.Builder(binding.root.context).apply {
                        val notFilled = when {
                            firstName.isBlank() -> "Nama depan"
                            phoneNumberInput.isBlank() -> "Nomor telepon"
                            else -> "Email"
                        }
                        setTitle("Ups!")
                        setMessage(binding.root.context.getString(R.string.not_filled_required, notFilled))
                        setPositiveButton("Kembali") { _, _ -> }
                        create()
                        show()
                    }
                } else {
                    try {
                        val phoneNumber = phoneNumberInput.toLong()
                        viewModel.editContact(contact.id, Contact(kontak = ContactItem(firstName, lastName, email, phoneNumber)))

                        popupWindow.dismiss()
                        lifecycleOwner.lifecycleScope.launch {
                            viewModel.getSession().observe(lifecycleOwner) { user ->
                                mainActivity.loadContact(user.token)
                            }
                        }
                    } catch (e: NumberFormatException) {
                        AlertDialog.Builder(binding.root.context).apply {
                            setTitle("Ups!")
                            setMessage("Nomor telepon harus berupa angka")
                            setPositiveButton("Kembali") { _, _ -> }
                            create()
                            show()
                        }
                    }
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListContacts>() {
            override fun areItemsTheSame(oldItem: ListContacts, newItem: ListContacts): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ListContacts, newItem: ListContacts): Boolean {
                return oldItem == newItem
            }
        }
    }
}
