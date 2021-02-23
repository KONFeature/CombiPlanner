package com.nivelais.combiplanner.domain.usecases.task

import com.nivelais.combiplanner.domain.repositories.TaskRepository
import com.nivelais.combiplanner.domain.usecases.core.FlowableUseCase
import kotlinx.coroutines.FlowPreview

/**
 * Use case used to delete a task
 */
class DeleteTaskUseCase(
    private val taskRepository: TaskRepository
) : FlowableUseCase<DeleteTaskParams, DeleteTaskResult>() {

    @OptIn(FlowPreview::class)
    override suspend fun execute(params: DeleteTaskParams) {
        log.info("Deleting a task with param {}", params)
        taskRepository.delete(params.id)
        resultFlow.emit(DeleteTaskResult.SUCCESS)
    }
}

/**
 * Input for our use case
 */
data class DeleteTaskParams(
    val id: Long
)


/**
 * Possible result of this use case
 */
enum class DeleteTaskResult {
    WAITING,
    SUCCESS
}