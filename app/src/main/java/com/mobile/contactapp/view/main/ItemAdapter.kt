package com.mobile.contactapp.view.main

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mobile.contactapp.R
import com.mobile.contactapp.data.UserRepository
import com.mobile.contactapp.data.api.response.ListContacts
import com.mobile.contactapp.databinding.StoryCardBinding

class ItemAdapter (private val viewModel: MainViewModel) : ListAdapter<ListContacts, ItemAdapter.ItemViewHolder>(DIFF_CALLBACK) {

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

            }
            binding.contactsDeleteBtn.setOnClickListener {
                viewModel.deleteContact(contact.id)
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