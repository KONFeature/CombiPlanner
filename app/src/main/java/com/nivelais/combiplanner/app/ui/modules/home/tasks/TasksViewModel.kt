/*
 * Copyright 2020-2021 Quentin Nivelais
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
