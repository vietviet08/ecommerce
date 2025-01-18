package com.vietquoc.ecommerce.util

import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vietquoc.ecommerce.R
import com.vietquoc.ecommerce.activites.ShoppingActivity

fun Fragment.showBottomNavigationView() {
    val bottomNavigationView =
        (activity as ShoppingActivity).findViewById<BottomNavigationView>(R.id.bottomNavigation)
    bottomNavigationView.visibility = android.view.View.VISIBLE
}


fun Fragment.hideBottomNavigationView() {
    val bottomNavigationView =
        (activity as ShoppingActivity).findViewById<BottomNavigationView>(R.id.bottomNavigation)
    bottomNavigationView.visibility = android.view.View.GONE
}