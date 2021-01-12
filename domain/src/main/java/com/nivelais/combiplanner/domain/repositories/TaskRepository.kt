package com.nivelais.combiplanner.domain.repositories

import com.nivelais.combiplanner.domain.entities.Category
import com.nivelais.combiplanner.domain.entities.Task
import com.nivelais.combiplanner.domain.entities.TaskEntry
import com.nivelais.combiplanner.domain.exceptions.SaveTaskException
import kotlinx.coroutines.flow.Flow
import kotlin.jvm.Throws

/**
 * Repository used to manage our task
 */
interface TaskRepository {

    /**
     * Create a new task
     */
    @Throws(SaveTaskException::class)
    suspend fun create(
        name: String,
        category: Category,
        entries: List<TaskEntry>
    ): Task

    /**
     * Update a task
     */
    @Throws(SaveTaskException::class)
    suspend fun update(
        id: Long,
        name: String,
        category: Category,
        entries: List<TaskEntry>
    )

    /**
     * Find a task from it's id
     */
    suspend fun get(id: Long): Task?

    /**
     * Get all the task (For a given category or not)
     */
    suspend fun observeAll(category: Category?): Flow<List<Task>>

    /**
     * Delete a list of tasks from a category id
     */
    suspend fun deleteForCategory(categoryId: Long)

    /**
     * Delete a tasks from it's id
     */
    suspend fun delete(id: Long)
}