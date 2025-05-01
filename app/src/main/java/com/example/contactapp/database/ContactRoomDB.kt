package com.example.contactapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Contact::class], version = 2, exportSchema = false)
abstract class ContactRoomDB : RoomDatabase() {
    abstract fun contactDao(): ContactDao

    companion object {
        @Volatile
        private var INSTANCE: ContactRoomDB? = null

        @JvmStatic
        fun getDatabase(context: Context): ContactRoomDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ContactRoomDB::class.java,
                    "contact_db"
                )
                    .fallbackToDestructiveMigration() // Tambahkan ini untuk menghapus database lama
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}