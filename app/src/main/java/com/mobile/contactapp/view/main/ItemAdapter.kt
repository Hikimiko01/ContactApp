package com.mobile.contactapp.view.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mobile.contactapp.data.api.response.ListContacts
import com.mobile.contactapp.databinding.StoryCardBinding

class ItemAdapter : ListAdapter<ListContacts, ItemAdapter.ItemViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = StoryCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener{

        }
    }

    inner class ItemViewHolder(val binding: StoryCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(contact: ListContacts) {
            binding.tvItemName.text = "${contact.firstName} ${contact.lastName ?: ""}"
            binding.tvItemDesc.text = contact.phoneNumber.toString()
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