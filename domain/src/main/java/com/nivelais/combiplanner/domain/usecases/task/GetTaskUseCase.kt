package com.nivelais.combiplanner.domain.usecases.task

import com.nivelais.combiplanner.domain.entities.Task
import com.nivelais.combiplanner.domain.repositories.TaskRepository
import com.nivelais.combiplanner.domain.usecases.core.FlowableUseCase
import kotlinx.coroutines.FlowPreview

/**
 * Use case used to get a given task from it's id (or create a new one if no id present)
 */
class GetTaskUseCase(
    private val taskRepository: TaskRepository
) : FlowableUseCase<GetTaskParams, GetTaskResult>() {

    @OptIn(FlowPreview::class)
    override suspend fun execute(params: GetTaskParams) {
        log.debug("Fetching the task with id {}", params.id)
        resultFlow.emit(GetTaskResult.Loading)

        val task = params.id?.let { taskId ->
            taskRepository.get(taskId)
        }

        // TODO : Don't send back the entries of the task, to limit the amount of data passing by the flow

        task?.let {
            // If we got a task post it
            resultFlow.emit(GetTaskResult.Success(it))
        } ?: run {
            // Else post a not found status
            resultFlow.emit(GetTaskResult.NotFound)
        }
    }
}

/**
 * Input for our use case
 */
data class GetTaskParams(
    val id: Long? = null
)

/**
 * Result for this use case
 */
sealed class GetTaskResult(val task: Task?) {
    object Loading : GetTaskResult(null)
    class Success(task: Task) : GetTaskResult(task)
    object NotFound : GetTaskResult(null)
}