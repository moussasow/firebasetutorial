package com.mas.firebasetutorial.common

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
}