package com.example.contactapp.ui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.contactapp.R
import com.example.contactapp.database.Contact
import com.example.contactapp.databinding.ActivityInsertUpdateBinding
import com.example.contactapp.helper.DateHelper
import com.example.contactapp.helper.ViewModelFactory

class InsertUpdateActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_CONTACT = "extra_contact"
        const val ALERT_DIALOG_CLOSE = 10
        const val ALERT_DIALOG_DELETE = 20
        private const val TAG = "InsertUpdateActivity"
    }

    private var isEdit = false
    private var contact: Contact? = null
    private lateinit var contactInsertUpdateViewModel: ContactInsertUpdateViewModel
    private var _activityInsertUpdateBinding: ActivityInsertUpdateBinding? = null
    private val binding get() = _activityInsertUpdateBinding ?: throw IllegalStateException("Binding not initialized")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            _activityInsertUpdateBinding = ActivityInsertUpdateBinding.inflate(layoutInflater)
            setContentView(binding.root)
            Log.d(TAG, "onCreate: Binding initialized")

            contactInsertUpdateViewModel = obtainViewModel(this)

            contact = intent.getParcelableExtra(EXTRA_CONTACT)
            if (contact != null) {
                isEdit = true
                Log.d(TAG, "onCreate: Edit mode, contact: ${contact?.name}, isEdit: $isEdit")
            } else {
                contact = Contact()
                Log.d(TAG, "onCreate: Add mode, isEdit: $isEdit")
            }

            val actionBarTitle: String
            val btnTitle: String

            if (isEdit) {
                actionBarTitle = getString(R.string.change)
                btnTitle = getString(R.string.update)
                contact?.let { contact ->
                    binding.edtName.setText(contact.name)
                    binding.edtPhone.setText(contact.phone)
                }
            } else {
                actionBarTitle = getString(R.string.add)
                btnTitle = getString(R.string.save)
            }

            supportActionBar?.title = actionBarTitle
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            binding.btnSubmit.text = btnTitle
            Log.d(TAG, "onCreate: ActionBar and button configured")

            binding.btnSubmit.setOnClickListener {
                val name = binding.edtName.text.toString().trim()
                val phone = binding.edtPhone.text.toString().trim()
                when {
                    name.isEmpty() -> binding.edtName.error = getString(R.string.empty)
                    phone.isEmpty() -> binding.edtPhone.error = getString(R.string.empty)
                    else -> {
                        contact?.let { contact ->
                            contact.name = name
                            contact.phone = phone
                        }
                        if (isEdit) {
                            contactInsertUpdateViewModel.update(contact as Contact)
                            showToast(getString(R.string.changed))
                        } else {
                            contact?.let { contact ->
                                contact.date = DateHelper.getCurrentDate()
                            }
                            contactInsertUpdateViewModel.insert(contact as Contact)
                            showToast(getString(R.string.added))
                        }
                        finish()
                    }
                }
            }

            onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    showAlertDialog(ALERT_DIALOG_CLOSE)
                }
            })

            invalidateOptionsMenu()
        } catch (e: Exception) {
            Log.e(TAG, "Error in onCreate: ${e.message}", e)
            throw e
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        Log.d(TAG, "onCreateOptionsMenu called, isEdit: $isEdit")
        if (isEdit) {
            try {
                menuInflater.inflate(R.menu.menu_form, menu)
                Log.d(TAG, "Menu inflated for edit mode")
            } catch (e: Exception) {
                Log.e(TAG, "Error inflating menu: ${e.message}", e)
                return false
            }
        } else {
            Log.d(TAG, "Menu not inflated because not in edit mode")
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d(TAG, "onOptionsItemSelected: Item ID = ${item.itemId}")
        when (item.itemId) {
            R.id.action_delete -> {
                Log.d(TAG, "Delete action clicked")
                showAlertDialog(ALERT_DIALOG_DELETE)
                return true
            }
            android.R.id.home -> {
                showAlertDialog(ALERT_DIALOG_CLOSE)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun showAlertDialog(type: Int) {
        val isDialogClose = type == ALERT_DIALOG_CLOSE
        val dialogTitle: String
        val dialogMessage: String

        if (isDialogClose) {
            dialogTitle = getString(R.string.cancel)
            dialogMessage = getString(R.string.message_cancel)
        } else {
            dialogTitle = getString(R.string.delete)
            dialogMessage = getString(R.string.message_delete)
        }

        try {
            val alertDialogBuilder = AlertDialog.Builder(this)
            with(alertDialogBuilder) {
                setTitle(dialogTitle)
                setMessage(dialogMessage)
                setCancelable(false)
                setPositiveButton(getString(R.string.yes)) { _, _ ->
                    if (!isDialogClose) {
                        Log.d(TAG, "Deleting contact: ${contact?.name}")
                        contact?.let { contactInsertUpdateViewModel.delete(it) }
                            ?: Log.e(TAG, "Contact is null during delete")
                        showToast(getString(R.string.deleted))
                    }
                    finish()
                }
                setNegativeButton(getString(R.string.no)) { dialog, _ ->
                    dialog.dismiss()
                }
            }
            alertDialogBuilder.create().show()
        } catch (e: Exception) {
            Log.e(TAG, "Error showing dialog: ${e.message}", e)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityInsertUpdateBinding = null
    }

    private fun obtainViewModel(activity: AppCompatActivity): ContactInsertUpdateViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(ContactInsertUpdateViewModel::class.java)
    }
}