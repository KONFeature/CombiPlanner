package com.nivelais.combiplanner.app.ui.modules.home.tasks

import com.nivelais.combiplanner.app.ui.modules.main.GenericViewModel
import com.nivelais.combiplanner.domain.entities.Category
import com.nivelais.combiplanner.domain.usecases.task.GetTasksParams
import com.nivelais.combiplanner.domain.usecases.task.GetTasksUseCase
import org.koin.core.scope.inject

class TasksViewModel : GenericViewModel() {

    // View model used to get all the task
    private val getTasksUseCase: GetTasksUseCase by inject()

    /**
     * State flow of our tasks
     */
    val tasksFlow = getTasksUseCase.stateFlow

    /**
     * Call the repository to fetch all of oru task
     */
    fun fetchTasks(category: Category? = null) =
        getTasksUseCase.run(GetTasksParams(category = category))

    override fun clearUseCases() {
        getTasksUseCase.clear()
    }
}