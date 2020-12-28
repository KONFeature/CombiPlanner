package com.nivelais.combiplanner.app.ui.modules.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.nivelais.combiplanner.app.ui.modules.main.GenericViewModel
import com.nivelais.combiplanner.domain.entities.Category
import com.nivelais.combiplanner.domain.usecases.GetCategoriesUseCase
import org.koin.core.scope.inject

class HomeViewModel : GenericViewModel() {

    // View model used to get all the categories
    private val getCategoriesUseCase: GetCategoriesUseCase by inject()

    // State for the current category selected
    val selectedCategoryState: MutableState<Category?> = mutableStateOf(null)

    // State for the visibility of the filter card
    val filterVisibilityState = mutableStateOf(false)

    /**
     * State flow of our categories
     */
    val categoriesFlow = getCategoriesUseCase.stateFlow

    // Load all the categories
    fun fetchCategories() = getCategoriesUseCase.run(Unit)

    override fun clearUseCases() {
        getCategoriesUseCase.clear()
    }
}