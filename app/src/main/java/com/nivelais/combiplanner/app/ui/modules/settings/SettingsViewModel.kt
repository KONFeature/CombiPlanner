package com.nivelais.combiplanner.app.ui.modules.settings

import com.nivelais.combiplanner.app.ui.modules.main.GenericViewModel
import com.nivelais.combiplanner.domain.usecases.category.GetCategoriesUseCase
import org.koin.core.scope.inject

class SettingsViewModel : GenericViewModel() {

    // Use case to list all the categories
    private val getCategoriesUseCase: GetCategoriesUseCase by inject()

    /**
     * State flow of our categories
     */
    val categoriesFlow = getCategoriesUseCase.stateFlow

    init {
        // Load all the categories
        getCategoriesUseCase.run(Unit)
    }

    override fun clearUseCases() {
        getCategoriesUseCase.clear()
    }
}