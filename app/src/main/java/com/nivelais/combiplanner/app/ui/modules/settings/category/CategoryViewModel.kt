package com.nivelais.combiplanner.app.ui.modules.settings.category

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.nivelais.combiplanner.R
import com.nivelais.combiplanner.app.ui.modules.main.GenericViewModel
import com.nivelais.combiplanner.domain.entities.Category
import com.nivelais.combiplanner.domain.usecases.category.DeleteCategoryParams
import com.nivelais.combiplanner.domain.usecases.category.DeleteCategoryResult
import com.nivelais.combiplanner.domain.usecases.category.DeleteCategoryUseCase
import com.nivelais.combiplanner.domain.usecases.category.DeletionStrategy
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.core.scope.inject

class CategoryViewModel : GenericViewModel() {

    // Use case to remove a given category
    private val deleteCategoriesUseCase: DeleteCategoryUseCase by inject()

    // Is the deletion strategy required
    val deletionStrategyRequiredState = mutableStateOf(false)

    // If we got any error during the deletion put it here
    val deletionErrorResState: MutableState<Int?> = mutableStateOf(null)

    // State for the current category selected (for the migration strategy)
    val selectedCategoryState: MutableState<Category?> = mutableStateOf(null)

    // The current strategy
    val selectedDeletionStrategyState: MutableState<DeletionStrategy?> = mutableStateOf(null)

    init {
        // Collect the value of our deletion use case
        viewModelScope.launch {
            deleteCategoriesUseCase.stateFlow.collectLatest {
                when (it) {
                    DeleteCategoryResult.WAITING -> {
                        // Nothing
                    }
                    DeleteCategoryResult.SUCCESS -> {
                        // Reset our different state
                        deletionErrorResState.value = null
                        deletionStrategyRequiredState.value = false
                    }
                    DeleteCategoryResult.STRATEGY_REQUIRED -> {
                        deletionErrorResState.value = R.string.category_delete_error_strategy_required
                        deletionStrategyRequiredState.value = true
                    }
                    DeleteCategoryResult.INVALID_TARGET_CATEGORY -> {
                        deletionErrorResState.value = R.string.category_delete_error_invalid_target_category
                        deletionStrategyRequiredState.value = true
                    }
                    DeleteCategoryResult.ERROR -> {
                        deletionErrorResState.value = R.string.category_delete_error
                        deletionStrategyRequiredState.value = true
                    }
                }
            }
        }
    }

    /**
     * Call the use case to delete a category
     */
    fun deleteCategory(category: Category) {
        deleteCategoriesUseCase.run(
            DeleteCategoryParams(
                id = category.id,
                strategy = selectedDeletionStrategyState.value
            )
        )
    }

    override fun clearUseCases() {
        deleteCategoriesUseCase.clear()
    }
}