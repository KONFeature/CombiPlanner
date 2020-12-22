package com.nivelais.combiplanner.app.ui.modules.main

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nivelais.combiplanner.app.ui.modules.home.Home
import com.nivelais.combiplanner.app.ui.modules.settings.Settings

/**
 * Navigator composable widget, permit us to navigate through our app
 */
@Composable
fun Navigator() {

    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavBar(navController) }
    ) {
        NavHost(navController, startDestination = "home") {
            composable("home") { Home() }
            composable("settings") { Settings() }
        }
    }
}

object Routes {
    val HOME: Int by lazy { createRoute("home") }
    val SETTINGS: Int by lazy { createRoute("settings") }

    private fun createRoute(route: String) =
        "android-app://androidx.navigation.compose/$route".hashCode()
}