package com.nivelais.combiplanner.app.ui.modules.home

import com.nivelais.combiplanner.app.ui.modules.main.GenericViewModel
import com.nivelais.combiplanner.domain.usecases.GetCategoriesUseCase
import com.nivelais.combiplanner.domain.usecases.GetTasksUseCase
import org.koin.core.scope.inject

class HomeViewModel : GenericViewModel() {

    // View model used to get all the task
    private val getTasksUseCase: GetTasksUseCase by inject()

    // View model used to get all the ctaegories
    private val getCategoriesUseCase: GetCategoriesUseCase by inject()

    override fun clearUseCases() {
        //getCategoriesUseCase.clear()
    }
}