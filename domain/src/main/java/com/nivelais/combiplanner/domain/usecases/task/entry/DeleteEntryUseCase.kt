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
