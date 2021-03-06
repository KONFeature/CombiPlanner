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

import com.nivelais.combiplanner.domain.entities.TaskEntry
import com.nivelais.combiplanner.domain.repositories.TaskEntryRepository
import com.nivelais.combiplanner.domain.usecases.core.SimpleFlowUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Use case used to get all the entries of a given task from it's id
 */
class GetEntriesUseCase(
    override val observingScope: CoroutineScope,
    private val taskEntryRepository: TaskEntryRepository
) : SimpleFlowUseCase<GetEntriesParams, List<TaskEntry>>() {

    override fun execute(params: GetEntriesParams): Flow<List<TaskEntry>> {
        log.debug("Listening to the entries for the task with id {}", params.taskId)
        return taskEntryRepository.observeForTask(params.taskId)
            .map { entries -> entries.sortedBy { it.isDone } }
    }
}

/**
 * Input for our use case
 */
data class GetEntriesParams(
    val taskId: Long
)
