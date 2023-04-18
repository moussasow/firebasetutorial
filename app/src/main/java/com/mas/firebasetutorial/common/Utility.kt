package com.mas.firebasetutorial.common

import android.util.Patterns
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object Utility {

    @JvmStatic
    fun getUserFromEmail(email: String?): String {
        return email?.filterNot { it == '@' }?.filterNot { it == '.' } ?: "guesttestcom"
    }

    @JvmStatic
    fun createUserDbRef(email: String?) : DatabaseReference {
        return FirebaseDatabase.getInstance().reference
            .child("Android")
            .child("FirebaseTutorial")
            .child(Utility.getUserFromEmail(email))
    }

    fun checkUserInput(email: String?, password: String?, view: View) : Boolean {
        if (email.isNullOrEmpty()) {
            Snackbar.make(view, "Please enter an email address", Snackbar.LENGTH_SHORT).show()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Snackbar.make(view, "Please enter a valid email", Snackbar.LENGTH_SHORT).show()
            return false
        }

        if (password.isNullOrEmpty()) {
            Snackbar.make(view, "Please enter a password", Snackbar.LENGTH_SHORT).show()
            return false
        }

        if (password.length < 6) {
            Snackbar.make(view, "Password word is more than 6 letters", Snackbar.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    fun checkUserName(username: String?, view: View) : Boolean {
        if (username.isNullOrEmpty()) {
            Snackbar.make(view, "Username should not be empty!", Snackbar.LENGTH_SHORT).show()
            return false
        }

        return true
    }
}