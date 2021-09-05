package com.example.karbushev.utils

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