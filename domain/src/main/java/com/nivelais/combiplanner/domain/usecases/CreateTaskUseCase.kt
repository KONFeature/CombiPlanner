package com.nivelais.combiplanner.domain.usecases

import com.nivelais.combiplanner.domain.entities.Category
import com.nivelais.combiplanner.domain.repositories.TaskRepository
import com.nivelais.combiplanner.domain.usecases.core.FlowableUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview

/**
 * Use case used to create a new task
 */
class CreateTaskUseCase(
    override val observingScope: CoroutineScope,
    private val taskRepository: TaskRepository
) : FlowableUseCase<CreateTaskParams, CreateTaskResult>() {

    @OptIn(FlowPreview::class)
    override suspend fun execute(params: CreateTaskParams) {
        log.debug("Creating a new task with param {}", params)
        val createdTask = taskRepository.create(
            params.name,
            params.description,
            params.category
        )
        log.info("New task created {}", createdTask)
        resultFlow.emit(CreateTaskResult.SUCCESS)
    }

    override fun initialValue() = CreateTaskResult.LOADING
}

/**
 * Input for our use case
 */
data class CreateTaskParams(
    val name: String,
    val description: String?,
    val category: Category
)


/**
 * Possible result of this use case
 */
enum class CreateTaskResult {
    LOADING,
    SUCCESS
    // TODO : Error dependening on exception that can be throwned by the repository
}