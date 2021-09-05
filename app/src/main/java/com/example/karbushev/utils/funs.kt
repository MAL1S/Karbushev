package com.example.karbushev.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView

fun showToast(msg: String) {
    Toast.makeText(APP_ACTIVITY, msg, Toast.LENGTH_SHORT).show()
}

fun getSelectedItem(bottomNavigationView: BottomNavigationView): Int {
    val menu: Menu = bottomNavigationView.menu
    for (i in 0 until bottomNavigationView.menu.size()) {
        val menuItem: MenuItem = menu.getItem(i)
        if (menuItem.isChecked) {
            return i
        }
    }
    return 0
}

fun checkInternetConnection(): Boolean {
    val connectivityManager =
        APP_ACTIVITY.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

        val network = connectivityManager.activeNetwork ?: return false

        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

            else -> false
        }
    } else {

        @Suppress("DEPRECATION") val networkInfo =
            connectivityManager.activeNetworkInfo ?: return false
        @Suppress("DEPRECATION")
        return networkInfo.isConnected
    }
}