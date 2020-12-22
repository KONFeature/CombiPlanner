package com.nivelais.combiplanner.data.database.dao

import com.nivelais.combiplanner.data.database.entities.TaskEntity
import com.nivelais.combiplanner.data.database.entities.TaskEntity_
import com.nivelais.combiplanner.data.database.entities.TaskEntryEntity
import com.nivelais.combiplanner.data.database.observe
import com.nivelais.combiplanner.data.database.observeAll
import io.objectbox.Box
import io.objectbox.BoxStore
import io.objectbox.kotlin.boxFor
import io.objectbox.kotlin.query

/**
 * Dao used to manage our task object
 */
class TaskDao(boxStore: BoxStore) {

    /**
     * Box used to access our task
     */
    private val taskBox: Box<TaskEntity> = boxStore.boxFor()

    /**
     * Box used to access our taskEntry
     */
    private val taskEntryBox: Box<TaskEntryEntity> = boxStore.boxFor()

    /**
     * Save a task entity
     */
    fun save(taskEntity: TaskEntity): TaskEntity {
        // Save the task
        val taskId = taskBox.put(taskEntity)

        // Save all the entries if needed
        if (taskEntity.entries.hasPendingDbChanges()) {
            taskEntity.entries.applyChangesToDb()
        }

        // TODO : Check if we need to refetch it or if the entity is updated
        return taskBox.get(taskId);
    }

    /**
     * Get all the task entities we got in the database
     */
    fun getAll() = taskBox.observeAll()

    /**
     * Get all the task entities we got in the database
     */
    fun getForCategory(categoryId: Long) =
        taskBox.query {
            equal(TaskEntity_.categoryId, categoryId)
        }.observe()

}