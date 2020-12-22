package com.nivelais.combiplanner.domain.repositories

import com.nivelais.combiplanner.domain.entities.Category
import kotlinx.coroutines.flow.Flow

/**
 * Repository used to manage our category
 */
interface CategoryRepository {

    /**
     * Create a new category
     */
    suspend fun create(name: String, color: Long?): Category

    /**
     * Get all the category for a given
     */
    suspend fun observeAll(): Flow<List<Category>>

    /**
     * Delete a category
     */
    suspend fun delete(id: Long)
}