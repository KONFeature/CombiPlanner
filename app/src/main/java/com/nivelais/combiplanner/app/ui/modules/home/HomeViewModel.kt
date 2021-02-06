package com.nivelais.combiplanner.app.ui.modules.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.nivelais.combiplanner.app.ui.modules.main.GenericViewModel
import com.nivelais.combiplanner.domain.entities.Category

class HomeViewModel : GenericViewModel() {

    // State for the current category selected
    val selectedCategoryState: MutableState<Category?> = mutableStateOf(null)

    // State for the visibility of the filter card
    val filterVisibilityState = mutableStateOf(false)
}