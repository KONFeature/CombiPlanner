package com.nivelais.combiplanner.app.di

import com.nivelais.combiplanner.app.ui.modules.category.picker.CategoryPickerViewModel
import com.nivelais.combiplanner.app.ui.modules.home.HomeViewModel
import com.nivelais.combiplanner.app.ui.modules.home.tasks.TasksViewModel
import com.nivelais.combiplanner.app.ui.modules.settings.SettingsViewModel
import com.nivelais.combiplanner.app.ui.modules.settings.category.CategoryViewModel
import com.nivelais.combiplanner.app.ui.modules.settings.create_category.CreateCategoryViewModel
import com.nivelais.combiplanner.app.ui.modules.task.TaskViewModel
import com.nivelais.combiplanner.app.ui.modules.task.add_entry.AddEntryViewModel
import com.nivelais.combiplanner.app.ui.modules.task.entries.TaskEntriesViewModel
import com.nivelais.combiplanner.app.ui.modules.task.entry.TaskEntryViewModel
import com.nivelais.combiplanner.domain.entities.Category
import com.nivelais.combiplanner.domain.entities.TaskEntry
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
    viewModel { TaskEntriesViewModel() }
    viewModel { AddEntryViewModel() }
    factory { (taskEntry: TaskEntry) -> TaskEntryViewModel(taskEntry = taskEntry) }

    // Category picker
    viewModel { (initialCategory: Category?) -> CategoryPickerViewModel(initialCategory = initialCategory) }

}