package com.nivelais.combiplanner.domain.repositories

import com.nivelais.combiplanner.domain.entities.Category
import com.nivelais.combiplanner.domain.exceptions.CreateCategoryException
import com.nivelais.combiplanner.domain.exceptions.DeleteCategoryException
import kotlinx.coroutines.flow.Flow
import kotlin.jvm.Throws

/**
 * Repository used to manage our category
 */
interface CategoryRepository {

    /**
     * Create a new category
     */
    @Throws(CreateCategoryException::class)
    suspend fun create(name: String, color: Long?): Category

    /**
     * Get all the category for a given
     */
    suspend fun observeAll(): Flow<List<Category>>

    /**
     * Delete a category
     */
    @Throws(DeleteCategoryException::class)
    suspend fun delete(id: Long)
}