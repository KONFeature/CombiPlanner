package com.nivelais.combiplanner.domain.repositories

import com.nivelais.combiplanner.domain.entities.Category
import com.nivelais.combiplanner.domain.entities.Task
import kotlinx.coroutines.flow.Flow

/**
 * Repository used to manage our task
 */
interface TaskRepository {

    /**
     * Create a new task
     */
    suspend fun create(name: String, description: String?, category: Category): Task

    /**
     * Get all the task (For a given category or not)
     */
    suspend fun observeAll(category: Category?): Flow<List<Task>>
}