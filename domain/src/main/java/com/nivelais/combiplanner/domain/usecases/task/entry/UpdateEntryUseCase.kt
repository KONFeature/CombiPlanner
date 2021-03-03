package com.nivelais.combiplanner.domain.usecases.task.entry

import com.nivelais.combiplanner.domain.entities.Task
import com.nivelais.combiplanner.domain.repositories.PictureRepository
import com.nivelais.combiplanner.domain.repositories.TaskEntryRepository
import com.nivelais.combiplanner.domain.usecases.core.FlowableUseCase
import kotlinx.coroutines.FlowPreview

/**
 * Use case used to update a task entry
 */
class UpdateEntryUseCase(
    private val taskEntryRepository: TaskEntryRepository,
    private val pictureRepository: PictureRepository
) : FlowableUseCase<UpdateEntryParams, UpdateEntryResult>() {

    @OptIn(FlowPreview::class)
    override suspend fun execute(params: UpdateEntryParams) {
        log.info("Updating a task entry with param {}", params)
        resultFlow.emit(UpdateEntryResult.LOADING)

        // Insert all the picture and save them
        val pictures = params.rawPictures.map { rawPicture ->
            pictureRepository.create(rawPicture.first, rawPicture.second)
        }

        // Update the task entry
        taskEntryRepository.update(
            id = params.entryId,
            name = params.name,
            isDone = params.isDone,
            pictures = pictures,
            taskDependencies = params.taskDependencies
        )

        log.info("Tak entry updated with success")
        resultFlow.emit(UpdateEntryResult.SUCCESS)
    }
}

/**
 * Input for our use case
 */
data class UpdateEntryParams(
    val entryId: Long,
    val name: String? = null,
    val isDone: Boolean? = null,
    val rawPictures: List<Pair<ByteArray, String>> = emptyList(),
    val taskDependencies: List<Task>? = null
)


/**
 * Possible result of this use case
 */
enum class UpdateEntryResult {
    LOADING,
    SUCCESS,
}