package com.nivelais.combiplanner.app.ui.modules.settings

import com.nivelais.combiplanner.app.ui.modules.main.GenericViewModel
import com.nivelais.combiplanner.domain.entities.Category
import com.nivelais.combiplanner.domain.usecases.DeleteCategoryParams
import com.nivelais.combiplanner.domain.usecases.DeleteCategoryUseCase
import com.nivelais.combiplanner.domain.usecases.GetCategoriesUseCase
import org.koin.core.scope.inject

class SettingsViewModel : GenericViewModel() {

    // Use case to list all the categories
    private val getCategoriesUseCase: GetCategoriesUseCase by inject()

    // Use case to remove a given category
    private val deleteCategoriesUseCase: DeleteCategoryUseCase by inject()

    /**
     * State flow of our categories
     */
    val categoriesFlow = getCategoriesUseCase.stateFlow

    /**
     * Call the use case to fetch all of our category
     */
    fun fetchCategories() {
        getCategoriesUseCase.run(Unit)
    }

    /**
     * Call the use case to delete a category
     */
    fun deleteCategory(category: Category) {
        deleteCategoriesUseCase.run(DeleteCategoryParams(id = category.id))
    }

    override fun clearUseCases() {
        getCategoriesUseCase.clear()
        deleteCategoriesUseCase.clear()
    }
}