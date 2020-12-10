package com.nivelais.combiplanner.app.ui.modules.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nivelais.combiplanner.app.ui.modules.Route
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.viewmodel.ViewModelOwner
import org.koin.androidx.viewmodel.compat.ViewModelCompat.viewModel

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun Home(
    navController: NavController,
    homeViewModel: HomeViewModel = getViewModel()
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "Home",
            style = MaterialTheme.typography.h1
        )
        StateDemo(state = homeViewModel.useCaseFlow.collectAsState())
        Button(
            onClick = {
                navController.navigate(Route.SETTINGS)
            }) {
            Text(
                text = "Go to settings",
                style = MaterialTheme.typography.h6
            )
        }
        Button(
            onClick = {
                homeViewModel.launchTest()
            }) {
            Text(
                text = "Execute use case",
                style = MaterialTheme.typography.h6
            )
        }
    }
}

@Composable
fun StateDemo(state: State<String?>) {
    state.value?.let {
        Text(
            text = it,
            style = MaterialTheme.typography.h3
        )
    } ?: run {
        Text(
            text = "No value",
            style = MaterialTheme.typography.h3
        )
    }
}