package com.nivelais.combiplanner.app.ui.modules.home.tasks

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.nivelais.combiplanner.app.ui.modules.main.GenericViewModel
import com.nivelais.combiplanner.domain.entities.Category
import com.nivelais.combiplanner.domain.entities.Task
import com.nivelais.combiplanner.domain.usecases.task.GetTasksParams
import com.nivelais.combiplanner.domain.usecases.task.GetTasksUseCase
import kotlinx.coroutines.Job
import org.koin.core.scope.inject

class TasksViewModel : GenericViewModel() {

    // View model used to get all the task
    private val getTasksUseCase: GetTasksUseCase by inject()

    /**
     * List of tasks displayed on the UI
     */
    val tasks = SnapshotStateList<Task>()

    /**
     * Job that listener for our tasks
     */
    private var tasksListenerJob: Job? = null

    /**
     * Call the repository to fetch all of our task
     */
    fun fetchTasks(category: Category? = null) {
        tasksListenerJob = getTasksUseCase.observe(GetTasksParams(category = category)) {
            tasks.clear()
            tasks.addAll(it)
        }
    }

    override fun clearUseCases() {
        tasksListenerJob?.cancel()
    }
}