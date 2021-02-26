package com.nivelais.combiplanner.domain.usecases.task.entry

import com.nivelais.combiplanner.domain.repositories.TaskEntryRepository
import com.nivelais.combiplanner.domain.usecases.core.FlowableUseCase
import kotlinx.coroutines.FlowPreview

/**
 * Use case used to delete new task entry
 */
class DeleteEntryUseCase(
    private val taskEntryRepository: TaskEntryRepository
) : FlowableUseCase<DeleteEntryParams, DeleteEntryResult>() {

    @OptIn(FlowPreview::class)
    override suspend fun execute(params: DeleteEntryParams) {
        log.info("Deleting a task entry with param {}", params)
        resultFlow.emit(DeleteEntryResult.LOADING)
        taskEntryRepository.delete(
            id = params.entryId,
        )
        log.info("Tak entry deleted with success {}")
        resultFlow.emit(DeleteEntryResult.SUCCESS)
        // TODO : exception
    }
}

/**
 * Input for our use case
 */
data class DeleteEntryParams(
    val entryId: Long,
)


/**
 * Possible result of this use case
 */
enum class DeleteEntryResult {
    LOADING,
    SUCCESS,
}