package com.artemfilatov.environmentmonitor.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Dashboard
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Timeline
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(val route: String, val icon: ImageVector, val label: String)

object BottomNavItems {
    val items = listOf(
        BottomNavItem("main", Icons.Rounded.Home, "Головна"),
        BottomNavItem("current", Icons.Rounded.Timeline, "Поточне"),
        BottomNavItem("overview", Icons.Rounded.Dashboard, "Огляд")
    )
}
