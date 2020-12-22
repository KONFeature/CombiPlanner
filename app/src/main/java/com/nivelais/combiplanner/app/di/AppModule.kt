package com.nivelais.combiplanner.app.di

import com.nivelais.combiplanner.app.ui.modules.home.HomeViewModel
import com.nivelais.combiplanner.app.ui.modules.settings.SettingsViewModel
import com.nivelais.combiplanner.app.ui.modules.settings.create_category.CreateCategoryViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Inject all the dependency required by our app layer :
 *  - View Model
 *
 *  TODO : Scoped view model and use cases ?
 */
val appModule = module {

    // Home
    viewModel { HomeViewModel() }

    // Settings
    viewModel { SettingsViewModel() }
    viewModel { CreateCategoryViewModel() }

}