package com.example.contactapp.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.contactapp.database.Contact
import com.example.contactapp.databinding.ItemContactBinding
import com.example.contactapp.helper.ContactDiffCallback

class ContactAdapter : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {
    private val listContacts = ArrayList<Contact>()

    fun setListContacts(listContacts: List<Contact>) {
        val diffCallback = ContactDiffCallback(this.listContacts, listContacts)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listContacts.clear()
        this.listContacts.addAll(listContacts)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(listContacts[position])
    }

    override fun getItemCount(): Int = listContacts.size

    class ContactViewHolder(private val binding: ItemContactBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(contact: Contact) {
            binding.tvItemName.text = contact.name
            binding.tvItemPhone.text = contact.phone
            binding.tvItemDate.text = contact.date
            binding.cvItemContact.setOnClickListener {
                val intent = Intent(it.context, InsertUpdateActivity::class.java)
                intent.putExtra(InsertUpdateActivity.EXTRA_CONTACT, contact)
                it.context.startActivity(intent)
            }
        }
    }
}