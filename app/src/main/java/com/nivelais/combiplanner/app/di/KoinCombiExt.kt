package com.nivelais.combiplanner.app.di

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

/**
 * Function used to inject the nav controller in our koin scope
 */
@Composable
fun InjectNavController(
    navController: NavController
) {
    loadKoinModules(module {
        single(override = true) { navController }
    })
}