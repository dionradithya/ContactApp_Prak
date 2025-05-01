package com.example.contactapp.helper

import androidx.recyclerview.widget.DiffUtil
import com.example.contactapp.database.Contact

class ContactDiffCallback(
    private val mOldContactList: List<Contact>,
    private val mNewContactList: List<Contact>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = mOldContactList.size

    override fun getNewListSize(): Int = mNewContactList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldContactList[oldItemPosition].id == mNewContactList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldContact = mOldContactList[oldItemPosition]
        val newContact = mNewContactList[newItemPosition]
        return oldContact.name == newContact.name && oldContact.phone == newContact.phone
    }
}