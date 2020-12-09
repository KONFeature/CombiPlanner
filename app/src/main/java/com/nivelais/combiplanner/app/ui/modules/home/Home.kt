package com.nivelais.combiplanner.app.ui.modules.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nivelais.combiplanner.app.ui.modules.Route

@Composable
fun Home(navController: NavController) {

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
        Button(
            onClick = {
                navController.navigate(Route.SETTINGS)
            }) {
            Text(
                text = "Go to settings",
                style = MaterialTheme.typography.h6
            )
        }
    }

}