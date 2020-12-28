package com.nivelais.combiplanner.app.ui.modules.main

import androidx.annotation.StringRes
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
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.KEY_ROUTE
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigate
import com.nivelais.combiplanner.R

@Composable
fun BottomNavBar(navController: NavController) {
    BottomAppBar {
        // Create each of our nav buttoms
        navigationTargets().forEach { navTarget ->
            BottomNavigationItem(
                icon = { Icon(navTarget.icon) },
                selected = isCurrentRoute(navController = navController, target = navTarget),
                onClick = {
                    navController.navigate(navTarget.route) {
                        // Single top to prevent navigating on the same target again and again
                        launchSingleTop = true
                        // Popup all the backstack to the start destination
                        popUpTo = navController.graph.startDestination
                    }
                },
                label = {
                    Text(text = stringResource(id = navTarget.labelId))
                }
            )
        }
    }
}

@Composable
private fun isCurrentRoute(navController: NavController, target: BottomNavTarget): Boolean {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.arguments?.getString(KEY_ROUTE)
    return currentRoute == target.route;
}

/**
 * The different target of our bottom navbar
 */
sealed class BottomNavTarget(
    val route: String,
    val icon: ImageVector,
    @StringRes val labelId: Int
) {
    object Home : BottomNavTarget(Routes.HOME, Icons.Filled.Home, R.string.home_label)
    object Settings :
        BottomNavTarget(Routes.SETTINGS, Icons.Filled.Settings, R.string.settings_label)
}

/**
 * Extension on the route object to convert them to navigation target
 */
private fun navigationTargets() = listOf(
    BottomNavTarget.Home,
    BottomNavTarget.Settings
)