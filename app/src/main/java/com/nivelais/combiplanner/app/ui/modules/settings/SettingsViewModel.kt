package com.nivelais.combiplanner.app.ui.modules.settings

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.nivelais.combiplanner.app.ui.modules.main.GenericViewModel
import com.nivelais.combiplanner.domain.entities.Category
import com.nivelais.combiplanner.domain.usecases.category.GetCategoriesUseCase
import kotlinx.coroutines.Job
import org.koin.core.scope.inject

class SettingsViewModel : GenericViewModel() {

    // Use case to list all the categories
    private val getCategoriesUseCase: GetCategoriesUseCase by inject()

    /**
     * The list of categories displayed in our view
     */
    val categories: SnapshotStateList<Category> = SnapshotStateList()

    /**
     * Job that listen on our categories
     */
    private var categoriesListenerJob: Job? = null

    init {
        // Load all the categories
        categoriesListenerJob = getCategoriesUseCase.observe(Unit) {
            categories.clear()
            categories.addAll(it)
        }
    }

    override fun clearUseCases() {
        categoriesListenerJob?.cancel()
    }
}