package com.nivelais.combiplanner.app.ui.modules.settings.category

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.nivelais.combiplanner.R
import com.nivelais.combiplanner.app.ui.modules.main.GenericViewModel
import com.nivelais.combiplanner.app.utils.runAndCollect
import com.nivelais.combiplanner.domain.entities.Category
import com.nivelais.combiplanner.domain.usecases.category.DeleteCategoryParams
import com.nivelais.combiplanner.domain.usecases.category.DeleteCategoryResult
import com.nivelais.combiplanner.domain.usecases.category.DeleteCategoryUseCase
import com.nivelais.combiplanner.domain.usecases.category.DeletionStrategy
import kotlinx.coroutines.Job
import org.koin.core.scope.inject

class CategoryViewModel(
    private val category: Category,
    private val allCategories: List<Category>,
) : GenericViewModel() {

    // Use case to remove a given category
    private val deleteCategoriesUseCase: DeleteCategoryUseCase by inject()

    /**
     * State telling us if the deletion strategy is required for the deletion or not
     */
    val isDeletionStrategyRequiredState = mutableStateOf(false)

    /**
     * If an error occurred during the deletion we load the error ressources here
     */
    val deletionErrorResState: MutableState<Int?> = mutableStateOf(null)

    // State for the current category selected (for the migration strategy)
    val selectedCategoryState: MutableState<Category?> = mutableStateOf(null)

    // The current strategy
    val selectedDeletionStrategyState: MutableState<DeletionStrategy?> = mutableStateOf(null)

    // Val used to know if the deletion strategy require a category
    val isCategoryRequiredForDeletion: Boolean
        get() = isDeletionStrategyRequiredState.value &&
                selectedDeletionStrategyState.value is DeletionStrategy.Migrate

    // Val used to get the categories for the deletion strategy
    val categoriesAvailableForMigration: List<Category>
        get() = allCategories.filterNot { it.id == category.id }

    // The listener on the deletion scope
    private var deleteListenerJob: Job? = null

    /**
     * Call the use case to delete a category
     */
    fun deleteCategory(category: Category) {
        deleteListenerJob?.cancel()
        deleteListenerJob = deleteCategoriesUseCase.runAndCollect(
            DeleteCategoryParams(
                id = category.id,
                strategy = selectedDeletionStrategyState.value
            ),
            viewModelScope
        ) { deletionResult ->
            when (deletionResult) {
                DeleteCategoryResult.SUCCESS -> {
                    // Reset our different state
                    deletionErrorResState.value = null
                    isDeletionStrategyRequiredState.value = false
                }
                DeleteCategoryResult.STRATEGY_REQUIRED -> {
                    deletionErrorResState.value = R.string.category_delete_error_strategy_required
                    isDeletionStrategyRequiredState.value = true
                }
                DeleteCategoryResult.INVALID_TARGET_CATEGORY -> {
                    deletionErrorResState.value =
                        R.string.category_delete_error_invalid_target_category
                    isDeletionStrategyRequiredState.value = true
                }
                DeleteCategoryResult.ERROR -> {
                    deletionErrorResState.value = R.string.category_delete_error
                    isDeletionStrategyRequiredState.value = true
                }
            }
        }
    }

    /**
     * Dismiss the deletion strategy picker dialog
     */
    fun dismissDeletionStrategyDialog() {
        isDeletionStrategyRequiredState.value = false
        deletionErrorResState.value = null
        selectedDeletionStrategyState.value = null
        selectedCategoryState.value = null
    }

    override fun clearUseCases() {
        deleteListenerJob?.cancel()
        deleteCategoriesUseCase.clear()
    }
}