package com.nivelais.combiplanner.domain.repositories

import com.nivelais.combiplanner.domain.entities.Category
import com.nivelais.combiplanner.domain.entities.Task
import com.nivelais.combiplanner.domain.entities.TaskEntry
import com.nivelais.combiplanner.domain.exceptions.SaveTaskException
import kotlinx.coroutines.flow.Flow
import kotlin.jvm.Throws

/**
 * Repository used to manage the entry of our task
 */
interface TaskEntryRepository {

    /**
     * Create a new task entry for a task
     */
    suspend fun add(
        taskId: Long,
        name: String,
        isDone: Boolean = false,
        taskDependencies: List<Task> = emptyList()
    ): TaskEntry

    /**
     * Update a task entry
     */
    suspend fun update(
        id: Long,
        name: String?,
        isDone: Boolean?,
        taskDependencies: List<Task>?
    )

    /**
     * Delete a task entry from it's id
     */
    suspend fun delete(id: Long)

    /**
     * Get all the task entry (For a given task)
     */
    suspend fun observeForTask(taskId: Long): Flow<List<TaskEntry>>
}