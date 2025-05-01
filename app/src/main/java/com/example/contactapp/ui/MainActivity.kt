package com.example.contactapp.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.contactapp.database.Contact
import com.example.contactapp.databinding.ActivityMainBinding
import com.example.contactapp.helper.ViewModelFactory
import com.example.contactapp.repository.ContactRepository

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("Binding not initialized")
    private lateinit var adapter: ContactAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            _binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)
            Log.d("MainActivity", "onCreate: Binding initialized")

            adapter = ContactAdapter()
            binding.rvContacts.layoutManager = LinearLayoutManager(this)
            binding.rvContacts.setHasFixedSize(true)
            binding.rvContacts.adapter = adapter
            Log.d("MainActivity", "onCreate: RecyclerView configured")

            val contactMainViewModel = obtainViewModel(this)
            contactMainViewModel.getAllContacts().observe(this) { contacts ->
                Log.d("MainActivity", "Contacts observed: ${contacts?.size ?: 0}")
                if (contacts != null) {
                    adapter.setListContacts(contacts)
                } else {
                    Log.e("MainActivity", "Contacts LiveData returned null")
                }
            }

            binding.fabAdd.setOnClickListener {
                Log.d("MainActivity", "FAB clicked")
                val intent = Intent(this, InsertUpdateActivity::class.java)
                startActivity(intent)
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Error in onCreate: ${e.message}", e)
            throw e
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun obtainViewModel(activity: AppCompatActivity): ContactMainViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(ContactMainViewModel::class.java)
    }
}