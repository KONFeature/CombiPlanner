/*
 * Copyright 2020-2021 Quentin Nivelais
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nivelais.combiplanner.domain.usecases.task

import com.nivelais.combiplanner.domain.entities.Category
import com.nivelais.combiplanner.domain.exceptions.SaveTaskException
import com.nivelais.combiplanner.domain.repositories.TaskRepository
import com.nivelais.combiplanner.domain.usecases.core.FlowableUseCase
import kotlinx.coroutines.FlowPreview

/**
 * Use case used to create a new task
 */
class SaveTaskUseCase(
    private val taskRepository: TaskRepository
) : FlowableUseCase<SaveTaskParams, SaveTaskResult>() {

    @OptIn(FlowPreview::class)
    override suspend fun execute(params: SaveTaskParams) {
        log.info("Saving a new task with param {}", params)
        resultFlow.emit(SaveTaskResult.Loading)
        try {
            params.id?.let { initialId ->
                // Update operation
                taskRepository.update(
                    initialId,
                    params.name,
                    params.category
                )
                resultFlow.emit(SaveTaskResult.Success(initialId))
                log.info("Task updated with success {}")
            } ?: run {
                // Creation operation
                val createdTask = taskRepository.create(params.name)
                resultFlow.emit(SaveTaskResult.Success(createdTask.id))
                log.info("New task created {}", createdTask)
            }
        } catch (exception: SaveTaskException) {
            log.warn("Error when saving the task", exception)
            when (exception) {
                is SaveTaskException.InvalidNameException ->
                    resultFlow.emit(SaveTaskResult.InvalidName)
                is SaveTaskException.DuplicateNameException ->
                    resultFlow.emit(SaveTaskResult.DuplicateName)
            }
        } catch (exception: Throwable) {
            log.error("Unknown error occurred when saving the task", exception)
            resultFlow.emit(SaveTaskResult.Error)
        }
    }
}

/**
 * Input for our use case
 */
data class SaveTaskParams(
    val id: Long?,
    val name: String,
    val category: Category?
)

/**
 * Possible result of this use case
 */
sealed class SaveTaskResult {
    object Loading : SaveTaskResult()
    data class Success(val taskId: Long) : SaveTaskResult()
    object InvalidName : SaveTaskResult()
    object DuplicateName : SaveTaskResult()
    object Error : SaveTaskResult()
}
