/*
 * Copyright 2020-2021 Quentin Nivelais
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nivelais.combiplanner.data.database.dao

import com.nivelais.combiplanner.data.database.count
import com.nivelais.combiplanner.data.database.entities.TaskEntity
import com.nivelais.combiplanner.data.database.entities.TaskEntity_
import com.nivelais.combiplanner.data.database.getOrNull
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
        box.observe {
            equal(TaskEntity_.categoryId, categoryId)
        }

    /**
     * Get all the task entities we got in the database
     */
    fun getForCategory(categoryId: Long): List<TaskEntity> =
        box.query {
            equal(TaskEntity_.categoryId, categoryId)
        }.find()

    /**
     * Find a task by it's id (not using the direct getter of ObjectBox to prevent null pointer)
     */
    fun get(id: Long) = box.getOrNull {
        equal(TaskEntity_.id, id)
    }

    /**
     * Delete a task
     */
    fun delete(task: TaskEntity) = box.remove(task)

    /**
     * Count the number of task corresponding to the name
     */
    fun countByName(name: String) = box.count {
        equal(TaskEntity_.name, name)
    }
}
