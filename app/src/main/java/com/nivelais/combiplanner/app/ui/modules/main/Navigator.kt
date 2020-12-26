package com.nivelais.combiplanner.app.ui.modules.main

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.nivelais.combiplanner.app.ui.modules.home.HomePage
import com.nivelais.combiplanner.app.ui.modules.settings.SettingsPage
import com.nivelais.combiplanner.app.ui.modules.task.TaskPage

/**
 * Navigator composable widget, permit us to navigate through our app
 */
@Composable
fun Navigator() {

    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavBar(navController) }
    ) {
        NavHost(navController, startDestination = Routes.HOME) {
            // Home route
            composable(Routes.HOME) { HomePage(navController = navController) }
            // Settings route
            composable(Routes.SETTINGS) { SettingsPage() }
            // Task route (with the route id as optionnal arg)
            composable(
                Routes.TASK_W_PARAM,
                arguments = listOf(navArgument("taskId") {
                    type = NavType.LongType
                    defaultValue = -1L
                })
            ) { navBackStackEntry ->
                TaskPage(
                    navController = navController,
                    taskId = navBackStackEntry.arguments?.getLong("taskId")
                )
            }
        }
    }
}

object Routes {
    const val HOME = "home"
    const val SETTINGS = "settings"
    const val TASK = "task/"
    const val TASK_W_PARAM = "$TASK{taskId}"
}
