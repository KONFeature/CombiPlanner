package com.nivelais.combiplanner.domain.usecases.task.entry

import com.nivelais.combiplanner.domain.entities.Task
import com.nivelais.combiplanner.domain.repositories.TaskEntryRepository
import com.nivelais.combiplanner.domain.usecases.core.FlowableUseCase
import kotlinx.coroutines.FlowPreview

/**
 * Use case used to create a new task entry
 */
class CreateEntryUseCase(
    private val taskEntryRepository: TaskEntryRepository
) : FlowableUseCase<CreateEntryParams, CreateEntryResult>() {

    @OptIn(FlowPreview::class)
    override suspend fun execute(params: CreateEntryParams) {
        log.info("Creating a new task entry with param {}", params)
        val createdEntry = taskEntryRepository.add(
            taskId = params.taskId,
            name = params.name,
            isDone = params.isDone,
            taskDependencies = params.taskDependencies
        )
        log.info("Tak entry created with success {}", createdEntry)
        resultFlow.emit(CreateEntryResult.SUCCESS)
        // TODO : exception
    }
}

/**
 * Input for our use case
 */
data class CreateEntryParams(
    val taskId: Long,
    val name: String,
    val isDone: Boolean = false,
    val taskDependencies: List<Task> = emptyList()
)


/**
 * Possible result of this use case
 */
enum class CreateEntryResult {
    WAITING,
    SUCCESS,
}