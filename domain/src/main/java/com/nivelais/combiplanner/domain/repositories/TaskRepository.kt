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

import com.nivelais.combiplanner.domain.entities.Category
import com.nivelais.combiplanner.domain.entities.Task
import com.nivelais.combiplanner.domain.exceptions.SaveTaskException
import kotlinx.coroutines.flow.Flow

/**
 * Repository used to manage our task
 */
interface TaskRepository {

    /**
     * Create a new task
     */
    @Throws(SaveTaskException::class)
    suspend fun create(name: String): Task

    /**
     * Update a task
     */
    @Throws(SaveTaskException::class)
    suspend fun update(
        id: Long,
        name: String?,
        category: Category?
    )

    /**
     * Find a task from it's id
     */
    suspend fun get(id: Long): Task?

    /**
     * Get all the task (For a given category or not)
     */
    fun observeAll(category: Category?): Flow<List<Task>>

    /**
     * Delete a list of tasks from a category id
     */
    suspend fun deleteForCategory(categoryId: Long)

    /**
     * Delete a tasks from it's id
     */
    suspend fun delete(id: Long)
}
