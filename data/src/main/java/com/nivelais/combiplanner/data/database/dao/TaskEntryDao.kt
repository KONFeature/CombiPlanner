package com.nivelais.combiplanner.data.database.dao

import com.nivelais.combiplanner.data.database.entities.TaskEntryEntity
import io.objectbox.Box
import io.objectbox.BoxStore
import io.objectbox.kotlin.boxFor

/**
 * Dao used to manage our task object
 */
class TaskEntryDao(boxStore: BoxStore) {

    /**
     * Box used to access our task entries
     */
    private val box: Box<TaskEntryEntity> = boxStore.boxFor()

    /**
     * Save a list of task entries
     */
    fun save(entries: List<TaskEntryEntity>) = box.put(entries)

    /**
     * Delete a list of task entries
     */
    fun delete(entries: List<TaskEntryEntity>) = box.remove(entries)
}