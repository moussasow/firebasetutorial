package com.mas.firebasetutorial

import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.mas.firebasetutorial.databinding.ActivityMainBinding
import com.mas.firebasetutorial.features.Experimental
import com.mas.firebasetutorial.features.ForegroundCheckTask
import android.Manifest
import com.mas.firebasetutorial.ui.fragment.BaseFragment
import com.mas.firebasetutorial.ui.fragment.info.InfoFragment
import com.mas.firebasetutorial.ui.fragment.login.LoginFragment
import com.mas.firebasetutorial.ui.fragment.main.MainFragment
import com.mas.firebasetutorial.ui.fragment.map.MapFragment
import com.mas.firebasetutorial.ui.fragment.notification.NotificationFragment
import com.mas.firebasetutorial.ui.fragment.top.TopFragment
import com.mas.firebasetutorial.ui.fragment.upload.UploadFragment
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    companion object {
        const val BUNDLE_NOTIFICATION = "BUNDLE_NOTIFICATION"
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val extras = intent.extras
        var isNotification = false
        if (extras != null) {
            isNotification = extras.getBoolean(BUNDLE_NOTIFICATION, false)
        }

        if (isNotification) {
            binding.bottomNav.selectedItemId = R.id.nav_notification
            navigateFragment(NotificationFragment.newInstance("", ""))
        } else if (savedInstanceState == null) {
            val user = FirebaseAuth.getInstance().currentUser
            val fragment: BaseFragment = if (user != null) {
                TopFragment.newInstance(user.email, user.displayName)
            } else {
                MainFragment.newInstance()
            }
            navigateFragment(fragment)
        }

        initBottomNavigation()
        /*
        lifecycleScope.launch {
            val foreground = Experimental.isAppOnForeground(this@MainActivity)
            if (foreground) {
                Experimental.showToast(this@MainActivity,"The app is in the foreground")
            } else {
                Experimental.showToast(this@MainActivity, "The app is not in the foreground")
            }
        } */
        askNotificationPermission()
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

    // Declare the launcher at the top of your Activity/Fragment:
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
            Toast.makeText(this, "Permission guaranteed", Toast.LENGTH_SHORT).show()
        } else {
            // TODO: Inform user that that your app will not show notifications.
            Toast.makeText(this, "notifications Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}