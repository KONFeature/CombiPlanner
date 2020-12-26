package com.nivelais.combiplanner.data.database.dao

import com.nivelais.combiplanner.data.database.entities.TaskEntity
import com.nivelais.combiplanner.data.database.entities.TaskEntity_
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
    private val box: Box<TaskEntity> = boxStore.boxFor()

    /**
     * Save a task entity
     */
    fun save(taskEntity: TaskEntity): TaskEntity {
        // Save the task
        val taskId = box.put(taskEntity)

        // Save all the entries if needed
        if (taskEntity.entries.hasPendingDbChanges()) {
            taskEntity.entries.applyChangesToDb()
        }

        // TODO : Check if we need to refetch it or if the entity is updated
        return box.get(taskId);
    }

    /**
     * Get all the task entities we got in the database
     */
    fun getAll() = box.observeAll()

    /**
     * Get all the task entities we got in the database
     */
    fun getForCategory(categoryId: Long) =
        box.query {
            equal(TaskEntity_.categoryId, categoryId)
        }.observe()


    /**
     * Find a task by it's id (not using the direct getter of objectbox to prevent null pointer)
     */
    fun get(id: Long) = box.query {
        equal(TaskEntity_.id, id)
    }.findFirst()
}