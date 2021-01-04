package com.nivelais.combiplanner.domain.usecases.task

import com.nivelais.combiplanner.domain.entities.Category
import com.nivelais.combiplanner.domain.entities.TaskEntry
import com.nivelais.combiplanner.domain.exceptions.SaveTaskException
import com.nivelais.combiplanner.domain.repositories.TaskRepository
import com.nivelais.combiplanner.domain.usecases.core.FlowableUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview

/**
 * Use case used to create a new task
 */
class SaveTaskUseCase(
    override val observingScope: CoroutineScope,
    private val taskRepository: TaskRepository
) : FlowableUseCase<SaveTaskParams, SaveTaskResult>() {

    @OptIn(FlowPreview::class)
    override suspend fun execute(params: SaveTaskParams) {
        log.info("Saving a new task with param {}", params)
        try {
            params.id?.let { initialId ->
                // Update operation
                taskRepository.update(
                    initialId,
                    params.name,
                    params.description,
                    params.category,
                    params.entries
                )
                log.info("Task updated with success {}")
            } ?: run {
                // Creation operation
                val createdTask = taskRepository.create(
                    params.name,
                    params.description,
                    params.category,
                    params.entries
                )
                log.info("New task created {}", createdTask)
            }
            resultFlow.emit(SaveTaskResult.SUCCESS)
        } catch (exception: SaveTaskException) {
            log.warn("Error when saving the task", exception)
            when(exception) {
                is SaveTaskException.InvalidNameException ->
                    resultFlow.emit(SaveTaskResult.INVALID_NAME)
                is SaveTaskException.DuplicateNameException ->
                    resultFlow.emit(SaveTaskResult.DUPLICATE_NAME)
            }
        } catch (exception: Throwable) {
            log.error("Unknown error occurred when saving the task", exception)
            resultFlow.emit(SaveTaskResult.ERROR)
        }
    }

    override fun initialValue() = SaveTaskResult.WAITING
}

/**
 * Input for our use case
 */
data class SaveTaskParams(
    val id: Long?,
    val name: String,
    val description: String?,
    val entries: List<TaskEntry>,
    val category: Category
)


/**
 * Possible result of this use case
 */
enum class SaveTaskResult {
    WAITING,
    SUCCESS,
    INVALID_NAME,
    DUPLICATE_NAME,
    ERROR
}