package com.nivelais.combiplanner.data.database.dao

import com.nivelais.combiplanner.data.database.count
import com.nivelais.combiplanner.data.database.entities.CategoryEntity
import com.nivelais.combiplanner.data.database.entities.CategoryEntity_
import com.nivelais.combiplanner.data.database.getOrNull
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
    fun save(categoryEntity: CategoryEntity) = box.put(categoryEntity)

    /**
     * Get all the category entities we got in the database
     */
    fun getAll() = box.observeAll()

    /**
     * Delete a category from the database from it's id
     */
    fun deleteById(id: Long) = box.remove(id)

    /**
     * Count the number of category corresponding to the name
     */
    fun countByName(name: String) = box.count {
        equal(CategoryEntity_.name, name)
    }

    /**
     * Find a category by it's id (not using the direct getter of ObjectBox to prevent null pointer)
     */
    fun get(id: Long) = box.getOrNull {
        equal(CategoryEntity_.id, id)
    }

}