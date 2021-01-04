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
    fun save(taskEntity: TaskEntity) {
        // Save the task
        box.put(taskEntity)

        // Save all the entries if needed
        if (taskEntity.entries.hasPendingDbChanges()) {
            taskEntity.entries.applyChangesToDb()
        }
    }

    /**
     * Get all the task entities we got in the database
     */
    fun getAll() = box.observeAll()

    /**
     * Get all the task entities we got in the database
     */
    fun observeForCategory(categoryId: Long) =
        box.query {
            equal(TaskEntity_.categoryId, categoryId)
        }.observe()

    /**
     * Get all the task entities we got in the database
     */
    fun getForCategory(categoryId: Long): List<TaskEntity> =
        box.query {
            equal(TaskEntity_.categoryId, categoryId)
        }.find()


    /**
     * Find a task by it's id (not using the direct getter of objectbox to prevent null pointer)
     */
    fun get(id: Long) = box.query {
        equal(TaskEntity_.id, id)
    }.findFirst()

    /**
     * Delete a task
     */
    fun delete(task: TaskEntity) = box.remove(task)

    /**
     * Count the number of task corresponding to the name
     */
    fun countByName(name: String) = box.query {
        equal(TaskEntity_.name, name)
    }.count()
}