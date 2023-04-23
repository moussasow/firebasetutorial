package com.mas.firebasetutorial.features

import android.app.ActivityManager
import android.content.Context
import android.os.AsyncTask

/** Usage inside an activity
val foreground = ForegroundCheckTask().execute(this).get()
        if (foreground) {
            // The app is in the foreground
        } else {
            // The app is not in the foreground
        }

 */
class ForegroundCheckTask : AsyncTask<Context, Void, Boolean>() {
    override fun doInBackground(vararg params: Context): Boolean {
        val context = params[0].applicationContext
        return isAppOnForeground(context)
    }

    private fun isAppOnForeground(context: Context): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val appProcesses = activityManager.runningAppProcesses ?: return false
        val packageName = context.packageName
        for (appProcess in appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName == packageName) {
                return true
            }
        }
        return false
    }
}