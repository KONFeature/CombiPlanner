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