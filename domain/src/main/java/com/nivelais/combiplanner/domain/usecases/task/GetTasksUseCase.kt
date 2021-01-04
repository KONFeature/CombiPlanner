package com.nivelais.combiplanner.domain.usecases.task

import com.nivelais.combiplanner.domain.entities.Category
import com.nivelais.combiplanner.domain.entities.Task
import com.nivelais.combiplanner.domain.repositories.TaskRepository
import com.nivelais.combiplanner.domain.usecases.core.FlowableUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.emitAll

/**
 * Use case used to get all the task for a given category (or no category)
 */
class GetTasksUseCase(
    override val observingScope: CoroutineScope,
    private val taskRepository: TaskRepository
) : FlowableUseCase<GetTasksParams, List<Task>?>() {

    @OptIn(FlowPreview::class)
    override suspend fun execute(params: GetTasksParams) {
        log.debug("Listening to all the task for the category {}", params.category)

        // TODO : Check if in case of multiple execute we need to cancel previous flow
        val taskFlow = taskRepository.observeAll(params.category)
        resultFlow.emitAll(taskFlow)
    }

    override fun initialValue(): List<Task>? = null
}

/**
 * Input for our use case
 */
data class GetTasksParams(
    val category: Category? = null
)