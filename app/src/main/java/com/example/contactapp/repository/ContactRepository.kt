package com.example.contactapp.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.contactapp.database.Contact
import com.example.contactapp.database.ContactDao
import com.example.contactapp.database.ContactRoomDB
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ContactRepository(application: Application) {
    private val mContactDao: ContactDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = ContactRoomDB.getDatabase(application)
        mContactDao = db.contactDao()
    }

    fun getAllContacts(): LiveData<List<Contact>> = mContactDao.getAllContacts()

    fun insert(contact: Contact) {
        executorService.execute { mContactDao.insert(contact) }
    }

    fun update(contact: Contact) {
        executorService.execute { mContactDao.update(contact) }
    }

    fun delete(contact: Contact) {
        println("Repository: Deleting contact ${contact.name}") // Log untuk debugging
        executorService.execute { mContactDao.delete(contact) }
    }
}