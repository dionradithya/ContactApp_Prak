package com.example.contactapp.ui

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.contactapp.database.Contact
import com.example.contactapp.repository.ContactRepository

class ContactMainViewModel(application: Application) : ViewModel() {
    private val mContactRepository: ContactRepository = ContactRepository(application)

    fun getAllContacts(): LiveData<List<Contact>> = mContactRepository.getAllContacts()
}