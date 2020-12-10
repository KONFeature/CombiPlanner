package com.nivelais.combiplanner.app.ui.modules.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nivelais.combiplanner.domain.usecases.TestUseCase
import com.nivelais.combiplanner.domain.usecases.core.None
import kotlinx.coroutines.launch

class HomeViewModel constructor(
    private val testUseCase: TestUseCase,
) : ViewModel() {

    val useCaseFlow = testUseCase.stateFlow

    fun launchTest() {
        viewModelScope.launch {
            testUseCase.execute(None())
        }
    }

}