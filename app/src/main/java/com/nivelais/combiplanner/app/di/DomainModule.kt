package com.nivelais.combiplanner.app.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nivelais.combiplanner.app.ui.modules.home.HomeViewModel
import com.nivelais.combiplanner.app.ui.modules.settings.SettingsViewModel
import com.nivelais.combiplanner.app.ui.modules.settings.create_category.CreateCategoryViewModel
import com.nivelais.combiplanner.domain.usecases.*
import org.koin.core.scope.Scope
import org.koin.dsl.module

/**
 * Inject all the dependency required by our domain layer :
 *  - Use Case
 */
val domainModule = module {

    scope<HomeViewModel> {
        scoped { viewModelCoroutineScope() }
        scoped { GetTasksUseCase(get(), get()) }
        scoped { CreateTaskUseCase(get(), get()) }
    }

    scope<SettingsViewModel> {
        scoped { viewModelCoroutineScope() }
        scoped { GetCategoriesUseCase(get(), get()) }
        scoped { DeleteCategoryUseCase(get(), get()) }
    }

    scope<CreateCategoryViewModel> {
        scoped { viewModelCoroutineScope() }
        scoped { CreateCategoryUseCase(get(), get()) }
    }
}

/**
 * Get the coroutine scope definition inside a scoped view model
 */
private fun Scope.viewModelCoroutineScope() =
    (getSource() as ViewModel).viewModelScope