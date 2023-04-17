package com.mas.firebasetutorial

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.mas.firebasetutorial.ui.fragment.BaseFragment
import com.mas.firebasetutorial.ui.fragment.main.MainFragment
import com.mas.firebasetutorial.ui.fragment.top.TopFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            val user = FirebaseAuth.getInstance().currentUser
            val fragment: BaseFragment = if (user != null) {
                TopFragment.newInstance(user.email, user.displayName)
            } else {
                MainFragment.newInstance()
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commitNow()
        }
    }
}