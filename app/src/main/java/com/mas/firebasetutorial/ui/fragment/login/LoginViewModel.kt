package com.mas.firebasetutorial.ui.fragment.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import com.google.firebase.auth.FirebaseAuth
import com.mas.firebasetutorial.ui.fragment.login.data.LoginRepository

import com.mas.firebasetutorial.R

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(username: String, password: String, authType: Int) {
        val auth = FirebaseAuth.getInstance()
        if (authType == 1) {
            auth.signInWithEmailAndPassword(username, password)
                .addOnSuccessListener { task ->
                    _loginResult.value = LoginResult(
                        success = LoggedInUserView(
                            displayName = task.additionalUserInfo?.username
                                ?: "guest",
                            userName = username
                        )
                    )
                }
                .addOnFailureListener {
                    _loginResult.value = LoginResult(error = R.string.login_failed)
                }
        } else {
            auth.createUserWithEmailAndPassword(username, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = task.result.user
                    _loginResult.value = LoginResult(
                        success = LoggedInUserView(
                            displayName = user?.displayName
                                ?: "guest",
                            userName = username
                        )
                    )
                } else {
                    _loginResult.value = LoginResult(error = R.string.login_failed)
                }
            }
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains("@")) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}