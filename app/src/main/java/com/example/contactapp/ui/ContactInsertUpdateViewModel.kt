package com.example.contactapp.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.contactapp.database.Contact
import com.example.contactapp.repository.ContactRepository

class ContactInsertUpdateViewModel(application: Application) : ViewModel() {
    private val mContactRepository: ContactRepository = ContactRepository(application)

    fun insert(contact: Contact) {
        mContactRepository.insert(contact)
    }

    fun update(contact: Contact) {
        mContactRepository.update(contact)
    }

    fun delete(contact: Contact) {
        println("ViewModel: Deleting contact ${contact.name}") // Log untuk debugging
        mContactRepository.delete(contact)
    }
}