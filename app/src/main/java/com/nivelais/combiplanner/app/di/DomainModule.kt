package com.nivelais.combiplanner.app.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nivelais.combiplanner.app.ui.modules.category.picker.CategoryPickerViewModel
import com.nivelais.combiplanner.app.ui.modules.home.HomeViewModel
import com.nivelais.combiplanner.app.ui.modules.home.tasks.TasksViewModel
import com.nivelais.combiplanner.app.ui.modules.settings.SettingsViewModel
import com.nivelais.combiplanner.app.ui.modules.settings.category.CategoryViewModel
import com.nivelais.combiplanner.app.ui.modules.settings.create_category.CreateCategoryViewModel
import com.nivelais.combiplanner.app.ui.modules.task.TaskViewModel
import com.nivelais.combiplanner.app.ui.modules.task.entries.TaskEntriesViewModel
import com.nivelais.combiplanner.domain.entities.Category
import com.nivelais.combiplanner.domain.usecases.category.CreateCategoryUseCase
import com.nivelais.combiplanner.domain.usecases.category.DeleteCategoryUseCase
import com.nivelais.combiplanner.domain.usecases.category.GetCategoriesUseCase
import com.nivelais.combiplanner.domain.usecases.task.DeleteTaskUseCase
import com.nivelais.combiplanner.domain.usecases.task.GetTaskUseCase
import com.nivelais.combiplanner.domain.usecases.task.GetTasksUseCase
import com.nivelais.combiplanner.domain.usecases.task.SaveTaskUseCase
import com.nivelais.combiplanner.domain.usecases.task.entry.*
import org.koin.core.scope.Scope
import org.koin.dsl.module

/**
 * Inject all the dependency required by our domain layer :
 *  - Use Case
 */
val domainModule = module {
    // Home part
    scope<HomeViewModel> {
        scoped { viewModelCoroutineScope() }
    }
    scope<TasksViewModel> {
        scoped { viewModelCoroutineScope() }
        scoped { GetTasksUseCase(get(), get()) }
    }

    // Task part
    scope<TaskViewModel> {
        scoped { viewModelCoroutineScope() }
        scoped { SaveTaskUseCase(get(), get()) }
        scoped { GetTaskUseCase(get(), get()) }
        scoped { DeleteTaskUseCase(get(), get()) }
        scoped { CreateEntryUseCase(get(), get()) }
    }
    scope<TaskEntriesViewModel> {
        scoped { viewModelCoroutineScope() }
        scoped { GetEntriesUseCase(get(), get()) }
        scoped { CreateEntryUseCase(get(), get()) }
        scoped { UpdateEntryUseCase(get(), get()) }
        scoped { DeleteEntryUseCase(get(), get()) }
    }

    // Settings part
    scope<SettingsViewModel> {
        scoped { viewModelCoroutineScope() }
        scoped { GetCategoriesUseCase(get(), get()) }
    }
    scope<CategoryViewModel> {
        scoped { viewModelCoroutineScope() }
        scoped { DeleteCategoryUseCase(get(), get(), get()) }
    }
    scope<CreateCategoryViewModel> {
        scoped { viewModelCoroutineScope() }
        scoped { CreateCategoryUseCase(get(), get()) }
    }

    // Generic widgets
    scope<CategoryPickerViewModel> {
        scoped { viewModelCoroutineScope() }
        scoped { GetCategoriesUseCase(get(), get()) }
    }
}

/**
 * Get the coroutine scope definition inside a scoped view model
 */
private fun Scope.viewModelCoroutineScope() =
    (getSource() as ViewModel).viewModelScope