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
