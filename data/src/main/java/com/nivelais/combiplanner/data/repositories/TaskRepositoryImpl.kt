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
package com.nivelais.combiplanner.data.repositories

import com.nivelais.combiplanner.data.database.dao.TaskDao
import com.nivelais.combiplanner.data.database.dao.TaskEntryDao
import com.nivelais.combiplanner.data.database.entities.TaskEntity
import com.nivelais.combiplanner.data.database.mapper.TaskDatabaseMapper
import com.nivelais.combiplanner.data.database.mapper.TaskEntryDatabaseMapper
import com.nivelais.combiplanner.domain.entities.Category
import com.nivelais.combiplanner.domain.entities.Task
import com.nivelais.combiplanner.domain.exceptions.SaveTaskException
import com.nivelais.combiplanner.domain.repositories.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mapstruct.factory.Mappers

/**
 * @inheritDoc
 */
class TaskRepositoryImpl(
    private val taskDao: TaskDao,
    private val taskEntryDao: TaskEntryDao,
    private val taskDatabaseMapper: TaskDatabaseMapper = Mappers.getMapper(
        TaskDatabaseMapper::class.java
    ),
    private val taskEntryDatabaseMapper: TaskEntryDatabaseMapper = Mappers.getMapper(
        TaskEntryDatabaseMapper::class.java
    )
) : TaskRepository {

    /**
     * @inheritDoc
     */
    @Throws(SaveTaskException::class)
    override suspend fun create(name: String): Task {
        checkName(name, true)
        // Create the task entity
        val entity = TaskEntity(name = name)

        // Insert it in the database and return the mapped result
        taskDao.save(entity)
        return taskDatabaseMapper.entityToData(entity)
    }

    /**
     * @inheritDoc
     */
    @Throws(SaveTaskException::class)
    override suspend fun update(
        id: Long,
        name: String?,
        category: Category?
    ) {
        // Get the entity for this id
        taskDao.get(id)?.let { taskEntity ->
            // Check and update the name if provided
            name?.let {
                checkName(it, false)
                taskEntity.name = it
            }
            // Update the category if provided
            category?.let {
                taskEntity.category.targetId = category.id
            }
            // Then save the updated entity
            taskDao.save(taskEntity)
        }
    }

    /**
     * Check a task name
     */
    @Throws(SaveTaskException::class)
    private fun checkName(name: String, needToBeUnique: Boolean) {
        // Empty name not accepted
        if (name.isBlank()) {
            throw SaveTaskException.InvalidNameException
        }
        // In the case of a task creation also check for name duplication
        if (needToBeUnique && taskDao.countByName(name) > 0) {
            throw SaveTaskException.DuplicateNameException
        }
    }

    /**
     * @inheritDoc
     */
    override suspend fun get(id: Long): Task? {
        val taskEntity = taskDao.get(id)
        return taskEntity?.let { taskDatabaseMapper.entityToData(it) }
    }

    /**
     * @inheritDoc
     */
    override fun observeAll(category: Category?): Flow<List<Task>> {
        // Get the right flow
        val entitiesFlow = category?.let {
            taskDao.observeForCategory(it.id)
        } ?: run {
            taskDao.getAll()
        }

        // Return the mapped entities flow
        return entitiesFlow.map { entities ->
            taskDatabaseMapper.entitiesToDatas(entities)
        }
    }

    /**
     * @inheritDoc
     */
    override suspend fun deleteForCategory(categoryId: Long) {
        val tasks = taskDao.getForCategory(categoryId)
        if (tasks.isEmpty()) return // Exit directly if we got no task to remove

        tasks.forEach { taskEntity ->
            deleteTaskAndEntries(taskEntity)
        }
    }

    /**
     * @inheritDoc
     */
    override suspend fun delete(id: Long) {
        // Get the task
        val task = taskDao.get(id)
        task?.let { deleteTaskAndEntries(it) }
    }

    /**
     * Delete a task entity with the associated entries
     */
    private fun deleteTaskAndEntries(taskEntity: TaskEntity) {
        // Drop all the task entries first
        taskEntryDao.delete(taskEntity.entries)
        // Then delete the task
        taskDao.delete(taskEntity)
    }
}
