package com.nivelais.combiplanner.app.ui.modules.settings

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

@Composable
fun Settings(navController: NavController) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.h1
        )
        Button(
            onClick = {
                navController.popBackStack()
            }) {
            Text(
                text = "Go back",
                style = MaterialTheme.typography.h6
            )
        }
    }

}