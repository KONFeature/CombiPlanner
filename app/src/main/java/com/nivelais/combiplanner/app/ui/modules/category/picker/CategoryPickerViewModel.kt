package com.nivelais.combiplanner.app.ui.modules.category.picker

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import com.nivelais.combiplanner.app.ui.modules.main.GenericViewModel
import com.nivelais.combiplanner.domain.entities.Category
import com.nivelais.combiplanner.domain.usecases.category.GetCategoriesUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.core.scope.inject

class CategoryPickerViewModel(
    initialCategory: Category? = null
) : GenericViewModel() {

    // View model used to get all the categories
    private val getCategoriesUseCase: GetCategoriesUseCase by inject()

    // State for the current category selected
    val selectedCategoryState: MutableState<Category?> = mutableStateOf(initialCategory)

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
        categoriesListenerJob = viewModelScope.launch {
            getCategoriesUseCase.launch(Unit).collect {
                categories.clear()
                categories.addAll(it)
            }
        }
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
        categoriesListenerJob?.cancel()
    }
}