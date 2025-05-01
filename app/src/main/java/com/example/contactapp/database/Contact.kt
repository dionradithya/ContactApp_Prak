package com.example.contactapp.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "contact")
data class Contact(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var name: String = "",
    var phone: String = "",
    var date: String = ""
) : Parcelable