package com.nivelais.combiplanner.app.ui.modules.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.onActive
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import com.nivelais.combiplanner.app.ui.modules.home.tasks.Tasks
import com.nivelais.combiplanner.app.ui.modules.main.Routes
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun HomePage(
    homeViewModel: HomeViewModel = getViewModel(),
    navController: NavController
) {
    onActive {
        // TODO : Fetch category for filter
        homeViewModel.log.info("nanana")
    }
    // TODO : Category filter for displayed task ?

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp).fillMaxWidth()
    ) {
        Text(
            text = "Home",
            style = MaterialTheme.typography.h1
        )
        // Show the tasks
        Tasks(navController = navController)

        // Button used to launch the task creation
        FloatingActionButton(
            onClick = {
                // Go the task screen with an empty id
                navController.navigate(Routes.TASK + 0)
            }
        ) {
            Icon(Icons.Filled.Add)
        }
    }
}