package com.nivelais.combiplanner.domain.usecases.task

import com.nivelais.combiplanner.domain.entities.Category
import com.nivelais.combiplanner.domain.entities.Task
import com.nivelais.combiplanner.domain.repositories.TaskRepository
import com.nivelais.combiplanner.domain.usecases.core.SimpleFlowUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow

/**
 * Use case used to get all the task for a given category (or no category)
 */
class GetTasksUseCase(
    override val observingScope: CoroutineScope,
    private val taskRepository: TaskRepository
) : SimpleFlowUseCase<GetTasksParams, List<Task>>() {

    @OptIn(FlowPreview::class)
    override fun execute(params: GetTasksParams): Flow<List<Task>> {
        log.debug("Listening to all the task for the category {}", params.category)
        return taskRepository.observeAll(params.category)
    }
}

/**
 * Input for our use case
 */
data class GetTasksParams(
    val category: Category? = null
)