package com.nivelais.combiplanner.app.ui.modules.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.nivelais.combiplanner.app.ui.modules.main.GenericViewModel
import com.nivelais.combiplanner.domain.entities.Category
import com.nivelais.combiplanner.domain.usecases.category.GetCategoriesUseCase
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

    init {
        // Load all the categories
        getCategoriesUseCase.run(Unit)
    }

    /**
     * Handle the selection of a new category
     */
    fun onCategorySelected(category: Category) {
        if (selectedCategoryState.value == category) {
            selectedCategoryState.value = null
        } else {
            selectedCategoryState.value = category
        }
    }

    override fun clearUseCases() {
        getCategoriesUseCase.clear()
    }
}