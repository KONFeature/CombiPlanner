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
package com.nivelais.combiplanner.domain.usecases.task.entry

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
