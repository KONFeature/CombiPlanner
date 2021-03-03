package com.nivelais.combiplanner.domain.usecases.task.entry

import com.nivelais.combiplanner.domain.entities.Task
import com.nivelais.combiplanner.domain.repositories.PictureRepository
import com.nivelais.combiplanner.domain.repositories.TaskEntryRepository
import com.nivelais.combiplanner.domain.usecases.core.FlowableUseCase
import kotlinx.coroutines.FlowPreview

/**
 * Use case used to create a new task entry
 */
class AddEntryUseCase(
    private val taskEntryRepository: TaskEntryRepository,
) : FlowableUseCase<AddEntryParams, AddEntryResult>() {

    @OptIn(FlowPreview::class)
    override suspend fun execute(params: AddEntryParams) {
        resultFlow.emit(AddEntryResult.WAITING)
        log.info("Adding a new task entry with param {}", params)

        // Create the entry with the right infos
        val createdEntry = taskEntryRepository.add(
            taskId = params.taskId,
        )

        log.info("Tak entry added with success {}", createdEntry)
        resultFlow.emit(AddEntryResult.SUCCESS)
        // TODO : exception
    }
}

/**
 * Input for our use case
 */
data class AddEntryParams(
    val taskId: Long,
)


/**
 * Possible result of this use case
 */
enum class AddEntryResult {
    WAITING,
    SUCCESS,
}