package com.irene.myshoppinglists.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(var title: String, var icon: ImageVector, var screen_route:String){

    object Lists: BottomNavItem("Home", Icons.Outlined.Home,"home")
    object Friends: BottomNavItem("My Devices", Icons.Outlined.Phone,"my_devices")
    object Products: BottomNavItem("Map", Icons.Outlined.LocationOn,"map")
}