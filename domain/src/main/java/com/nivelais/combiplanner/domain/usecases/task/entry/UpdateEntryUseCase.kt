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
 * Use case used to update a task entry
 */
class UpdateEntryUseCase(
    private val taskEntryRepository: TaskEntryRepository,
) : FlowableUseCase<UpdateEntryParams, UpdateEntryResult>() {

    @OptIn(FlowPreview::class)
    override suspend fun execute(params: UpdateEntryParams) {
        log.info("Updating a task entry with param {}", params)
        resultFlow.emit(UpdateEntryResult.LOADING)

        // Update the task entry
        taskEntryRepository.update(
            id = params.entryId,
            name = params.name,
            isDone = params.isDone,
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
)

/**
 * Possible result of this use case
 */
enum class UpdateEntryResult {
    LOADING,
    SUCCESS,
}
