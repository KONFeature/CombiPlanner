package com.nivelais.combiplanner.app.ui.modules.main

import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavBar(navController: NavController) {
    BottomAppBar {
        // Get the current nav route
        val currentRouteId = currentRoute(navController)

        // Create each of our nav buttoms
        navigationTargets().forEach { navTarget ->
            val isCurrentTarget = navTarget.routeId == currentRouteId;
            BottomNavigationItem(
                icon = { Icon(navTarget.icon) },
                selected = isCurrentTarget,
                onClick = {
                    if (!isCurrentTarget) {
                        navController.navigate(navTarget.routeId, null, navTarget.navOptions())
                    }
                },
                label = {
                    Text(navTarget.label)
                }
            )
        }
    }
}

@Composable
private fun currentRoute(navController: NavController): Int? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.id
}

/**
 * Represent a target of our bottom nav bar
 */
private data class BottomNavigationTarget(
    val routeId: Int,
    val icon: ImageVector,
    val label: String
)

/**
 * Extension on the route object to convert them to navigation target
 */
private fun navigationTargets() = listOf(
    BottomNavigationTarget(Routes.HOME, Icons.Filled.Home, "Home"),
    BottomNavigationTarget(Routes.SETTINGS, Icons.Filled.Settings, "Settings"),
)

/**
 * Get the navigation options for our navigation target
 */
private fun BottomNavigationTarget.navOptions() =
    NavOptions.Builder()
        .setPopUpTo(Routes.HOME, routeId == Routes.HOME)
        .build()