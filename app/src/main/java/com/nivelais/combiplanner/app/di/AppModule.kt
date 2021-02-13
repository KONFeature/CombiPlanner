package com.nivelais.combiplanner.app.di

import com.nivelais.combiplanner.app.ui.modules.category.picker.CategoryPickerViewModel
import com.nivelais.combiplanner.app.ui.modules.home.HomeViewModel
import com.nivelais.combiplanner.app.ui.modules.home.tasks.TasksViewModel
import com.nivelais.combiplanner.app.ui.modules.settings.SettingsViewModel
import com.nivelais.combiplanner.app.ui.modules.settings.category.CategoryViewModel
import com.nivelais.combiplanner.app.ui.modules.settings.create_category.CreateCategoryViewModel
import com.nivelais.combiplanner.app.ui.modules.task.TaskViewModel
import com.nivelais.combiplanner.app.ui.modules.task.entries.TaskEntriesViewModel
import com.nivelais.combiplanner.domain.entities.Category
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Inject all the dependency required by our app layer :
 *  - View Model
 */
val appModule = module {

    // Home
    viewModel { HomeViewModel() }
    viewModel { TasksViewModel() }

    // Settings
    viewModel { SettingsViewModel() }
    viewModel { CategoryViewModel() }
    viewModel { CreateCategoryViewModel() }

    // Task
    viewModel { TaskViewModel() }
    viewModel { (taskId: Long?) -> TaskEntriesViewModel(taskId = taskId) }

    // Category picker
    viewModel { (initialCategory: Category?) -> CategoryPickerViewModel(initialCategory = initialCategory) }

}