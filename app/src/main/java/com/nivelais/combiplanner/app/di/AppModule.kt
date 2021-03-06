/*
 * Copyright 2020-2021 Quentin Nivelais
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
    viewModel { (category: Category, allCategories: List<Category>) ->
        CategoryViewModel(
            category = category,
            allCategories = allCategories
        )
    }
    viewModel { CreateCategoryViewModel() }

    // Task
    viewModel { TaskViewModel() }
    viewModel { TaskEntriesViewModel() }
    viewModel { AddEntryViewModel() }
    factory { TaskEntryViewModel() }

    // Category picker
    viewModel { (initialCategory: Category?) ->
        CategoryPickerViewModel(initialCategory = initialCategory)
    }
}
