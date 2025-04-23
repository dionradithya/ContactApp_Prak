package com.example.contactapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.contactapp.databinding.ActivityMainBinding
import java.util.UUID

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val contactList = ArrayList<Contact>()
    private lateinit var adapter: ContactAdapter
    private var contactIdCounter = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ContactAdapter(contactList) { id ->
            deleteContact(id)
        }
        binding.rvContacts.layoutManager = LinearLayoutManager(this)
        binding.rvContacts.adapter = adapter

        binding.btnAdd.setOnClickListener {
            val name = binding.etName.text.toString()
            val phone = binding.etPhone.text.toString()

            if (name.isNotEmpty() && phone.isNotEmpty()) {
                addContact(name, phone)
                binding.etName.text.clear()
                binding.etPhone.text.clear()
            }
        }

        addContact("Dion", "082158122525")
        addContact("Radithya", "150707111039")
    }

    private fun addContact(name: String, phone: String) {
        val contact = Contact(contactIdCounter++, name, phone)
        contactList.add(contact)
        adapter.notifyItemInserted(contactList.size - 1)
    }

    private fun deleteContact(id: Int) {
        val index = contactList.indexOfFirst { it.id == id }
        if (index != -1) {
            contactList.removeAt(index)
            adapter.notifyItemRemoved(index)
        }
    }
}