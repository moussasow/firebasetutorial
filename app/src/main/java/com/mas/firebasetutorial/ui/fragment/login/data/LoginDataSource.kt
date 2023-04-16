package com.mas.firebasetutorial.ui.fragment.login.data

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.mas.firebasetutorial.R
import com.mas.firebasetutorial.ui.fragment.login.LoggedInUserView
import com.mas.firebasetutorial.ui.fragment.login.LoginResult
import com.mas.firebasetutorial.ui.fragment.login.data.model.LoggedInUser
import java.io.IOException
import java.util.*

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun login(
        username: String,
        password: String,
        loginResult: MutableLiveData<LoginResult>
    ) {

        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(username, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = task.result.user
                loginResult.value = LoginResult(
                    success = LoggedInUserView(
                        displayName = user?.displayName
                            ?: "guest",
                        userName = username
                    )
                )
            } else {
                loginResult.value = LoginResult(error = R.string.login_failed)
            }
        }
    }


    fun logout() {
        // TODO: revoke authentication
    }
}