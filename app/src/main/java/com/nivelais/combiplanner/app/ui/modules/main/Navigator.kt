package com.nivelais.combiplanner.app.ui.modules.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.*
import com.nivelais.combiplanner.app.di.InjectNavController
import com.nivelais.combiplanner.app.ui.modules.home.HomePage
import com.nivelais.combiplanner.app.ui.modules.settings.SettingsPage
import com.nivelais.combiplanner.app.ui.modules.task.TaskPage

/**
 * Navigator composable widget, permit us to navigate through our app
 */
@Composable
fun Navigator() {
    val navController = rememberNavController()
    // Inject the nav controller in our koin context
    InjectNavController(navController = navController)

    Scaffold(
        bottomBar = { BottomNavBar() }
    ) {
        // Encapsulate the nav host in a box to prevent bottom hidden under bottom bar
        Box(
            modifier = Modifier.padding(bottom = 48.dp)
        ) {
            NavHost(
                navController,
                startDestination = Route.Home.name
            ) {
                // Home route
                composable(Route.Home.name) { HomePage() }
                // Settings route
                composable(Route.Settings.name) { SettingsPage() }
                // Task route (with the route id as optional arg)
                composable(
                    Route.TaskHost.name,
                    arguments = listOf(navArgument("taskId") {
                        type = NavType.LongType
                        defaultValue = -1L
                    })
                ) { navBackStackEntry ->
                    TaskPage(
                        taskId = navBackStackEntry.arguments?.getLong("taskId")
                    )
                }
            }
        }
    }
}

// Class representing our different route
sealed class Route(val name: String) {
    object Home : Route("home")
    object Settings : Route("settings")

    class Task(taskId: Long = 0L) : Route("task/${taskId}")
    object TaskHost : Route("task/{taskId}")
}

// Extension on nav controller to use route directly
fun NavController.navigate(route: Route, builder: NavOptionsBuilder.() -> Unit = {}) =
    this.navigate(route = route.name, builder = builder)
