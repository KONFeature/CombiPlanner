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

import com.nivelais.combiplanner.data.database.entities.TaskEntity_
import com.nivelais.combiplanner.data.database.entities.TaskEntryEntity
import com.nivelais.combiplanner.data.database.entities.TaskEntryEntity_
import com.nivelais.combiplanner.data.database.getOrNull
import com.nivelais.combiplanner.data.database.observe
import io.objectbox.Box
import io.objectbox.BoxStore
import io.objectbox.kotlin.boxFor

/**
 * Dao used to manage our task entry object
 */
class TaskEntryDao(val boxStore: BoxStore) {

    /**
     * Box used to access our task entries
     */
    private val box: Box<TaskEntryEntity> = boxStore.boxFor()

    /**
     * Find a task entry by it's id (not using the direct getter of ObjectBox to prevent null pointer)
     */
    fun get(id: Long) = box.getOrNull {
        equal(TaskEntryEntity_.id, id)
    }

    /**
     * Save a list of task entries
     */
    fun save(entries: List<TaskEntryEntity>) = box.put(entries)

    /**
     * Save a task entry
     */
    fun save(entry: TaskEntryEntity) = box.put(entry)

    /**
     * Delete a list of task entries
     */
    fun delete(entries: List<TaskEntryEntity>) = box.remove(entries)

    /**
     * Delete a single task entry
     */
    fun delete(id: Long) = box.remove(id)

    /**
     * Get all the task entries entities we got in the database for a given task
     */
    fun observeForTask(taskId: Long) =
        box.observe {
            backlink(TaskEntity_.entries).equal(TaskEntity_.id, taskId)
        }
}
