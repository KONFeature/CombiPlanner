package com.nivelais.combiplanner.domain.usecases.task.entry

import com.nivelais.combiplanner.domain.entities.Task
import com.nivelais.combiplanner.domain.repositories.TaskEntryRepository
import com.nivelais.combiplanner.domain.usecases.core.FlowableUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview

/**
 * Use case used to update a task entry
 */
class UpdateEntryUseCase(
    override val observingScope: CoroutineScope,
    private val taskEntryRepository: TaskEntryRepository
) : FlowableUseCase<UpdateEntryParams, UpdateEntryResult>() {

    @OptIn(FlowPreview::class)
    override suspend fun execute(params: UpdateEntryParams) {
        log.info("Updating a task entry with param {}", params)
        taskEntryRepository.update(
            id = params.entryId,
            name = params.name,
            isDone = params.isDone,
            taskDependencies = params.taskDependencies
        )
        log.info("Tak entry updated with success")
        resultFlow.emit(UpdateEntryResult.SUCCESS)
    }

    override fun initialValue() = UpdateEntryResult.WAITING
}

/**
 * Input for our use case
 */
data class UpdateEntryParams(
    val entryId: Long,
    val name: String? = null,
    val isDone: Boolean? = null,
    val taskDependencies: List<Task>? = null
)


/**
 * Possible result of this use case
 */
enum class UpdateEntryResult {
    WAITING,
    SUCCESS,
}