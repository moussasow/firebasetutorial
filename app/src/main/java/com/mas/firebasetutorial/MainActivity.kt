package com.mas.firebasetutorial

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.mas.firebasetutorial.databinding.ActivityMainBinding
import com.mas.firebasetutorial.ui.fragment.BaseFragment
import com.mas.firebasetutorial.ui.fragment.info.InfoFragment
import com.mas.firebasetutorial.ui.fragment.login.LoginFragment
import com.mas.firebasetutorial.ui.fragment.main.MainFragment
import com.mas.firebasetutorial.ui.fragment.map.MapFragment
import com.mas.firebasetutorial.ui.fragment.notification.NotificationFragment
import com.mas.firebasetutorial.ui.fragment.top.TopFragment
import com.mas.firebasetutorial.ui.fragment.upload.UploadFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            val user = FirebaseAuth.getInstance().currentUser
            val fragment: BaseFragment = if (user != null) {
                TopFragment.newInstance(user.email, user.displayName)
            } else {
                MainFragment.newInstance()
            }
            navigateFragment(fragment)
        }

        initBottomNavigation()
    }

    private fun initBottomNavigation() {
        binding.bottomNav.setOnItemSelectedListener { item ->
            val fragment: BaseFragment = when (item.itemId) {
                R.id.nav_home -> {
                    val user = FirebaseAuth.getInstance().currentUser
                    if (user != null) {
                        TopFragment.newInstance(user.email, user.displayName)
                    } else {
                        MainFragment.newInstance()
                    }
                }
                R.id.nav_upload -> UploadFragment()
                R.id.nav_map -> MapFragment()
                R.id.nav_notification -> NotificationFragment()
                R.id.nav_info -> InfoFragment()
                else -> MainFragment.newInstance()
            }

            navigateFragment(fragment)
            true
        }
    }

    private fun navigateFragment(fragment: BaseFragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commitNow()
    }
}