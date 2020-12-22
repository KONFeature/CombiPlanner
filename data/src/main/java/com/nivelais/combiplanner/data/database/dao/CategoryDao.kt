package com.nivelais.combiplanner.data.database.dao

import com.nivelais.combiplanner.data.database.entities.CategoryEntity
import com.nivelais.combiplanner.data.database.observeAll
import io.objectbox.Box
import io.objectbox.BoxStore
import io.objectbox.kotlin.boxFor

/**
 * Dao used to manage our category object
 */
class CategoryDao(boxStore: BoxStore) {

    /**
     * Box used to access our category table
     */
    private val box: Box<CategoryEntity> = boxStore.boxFor()

    /**
     * Save a new category
     */
    fun save(categoryEntity: CategoryEntity): CategoryEntity {
        val categoryId = box.put(categoryEntity)
        // TODO : Check if we need to refetch it or if the entity is updated
        return box.get(categoryId);
    }

    /**
     * Get all the category entities we got in the database
     */
    fun getAll() = box.observeAll()

    /**
     * Delete a category from the database from it's id
     */
    fun deleteById(id: Long) = box.remove(id)

}