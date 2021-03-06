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
package com.nivelais.combiplanner.domain.repositories

import com.nivelais.combiplanner.domain.entities.TaskEntry
import kotlinx.coroutines.flow.Flow

/**
 * Repository used to manage the entry of our task
 */
interface TaskEntryRepository {

    /**
     * Create a new task entry for a task
     */
    suspend fun add(
        taskId: Long
    ): TaskEntry

    /**
     * Update a task entry
     */
    suspend fun update(
        id: Long,
        name: String?,
        isDone: Boolean?,
    )

    /**
     * Delete a task entry from it's id
     */
    suspend fun delete(id: Long)

    /**
     * Get all the task entry (For a given task)
     */
    fun observeForTask(taskId: Long): Flow<List<TaskEntry>>
}
