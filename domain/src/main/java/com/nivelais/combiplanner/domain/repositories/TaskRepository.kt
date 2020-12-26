package com.nivelais.combiplanner.domain.repositories

import com.nivelais.combiplanner.domain.entities.Category
import com.nivelais.combiplanner.domain.entities.Task
import com.nivelais.combiplanner.domain.entities.TaskEntry
import kotlinx.coroutines.flow.Flow

/**
 * Repository used to manage our task
 */
interface TaskRepository {

    /**
     * Create a new task
     */
    suspend fun create(name: String, description: String?, category: Category, entries: List<TaskEntry>): Task

    /**
     * Find a task from it's id
     */
    suspend fun get(id: Long): Task?

    /**
     * Update a task
     */
    suspend fun update(id: Long, name: String, description: String?, category: Category, entries: List<TaskEntry>)

    /**
     * Get all the task (For a given category or not)
     */
    suspend fun observeAll(category: Category?): Flow<List<Task>>
}