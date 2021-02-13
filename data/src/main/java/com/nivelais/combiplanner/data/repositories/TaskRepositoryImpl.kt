package com.nivelais.combiplanner.data.repositories

import com.nivelais.combiplanner.data.database.dao.TaskDao
import com.nivelais.combiplanner.data.database.dao.TaskEntryDao
import com.nivelais.combiplanner.data.database.entities.TaskEntity
import com.nivelais.combiplanner.data.database.entities.TaskEntryEntity
import com.nivelais.combiplanner.data.database.mapper.TaskDatabaseMapper
import com.nivelais.combiplanner.data.database.mapper.TaskEntryDatabaseMapper
import com.nivelais.combiplanner.domain.entities.Category
import com.nivelais.combiplanner.domain.entities.Task
import com.nivelais.combiplanner.domain.entities.TaskEntry
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
    override suspend fun create(
        name: String,
        category: Category,
        entries: List<TaskEntry>
    ): Task {
        checkName(name, true)
        // Create the task entity
        val entity = TaskEntity(name = name)
        entity.category.targetId = category.id
        entity.entries.addAll(entries.map { TaskEntryEntity(name = it.name, isDone = it.isDone) })

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
        name: String,
        category: Category,
        entries: List<TaskEntry>
    ) {
        checkName(name, false)
        // Get the entity for this id
        val taskEntity = taskDao.get(id)
        taskEntity?.let { task ->
            // Update our resolved task
            task.name = name
            task.category.targetId = category.id
            // TODO : Track deletion
            // Update it's entries
//            val entryEntities = taskEntryDatabaseMapper.datasToEntities(entries)
//            taskEntryDao.save(entryEntities)
//            task.entries.clear()
//            task.entries.addAll(entryEntities)
//            task.entries.applyChangesToDb()
            taskDao.save(task)
        }
    }

    /**
     * Check a task name
     */
    @Throws(SaveTaskException::class)
    private fun checkName(name: String, isTaskCreation: Boolean) {
        // Empty name not accepted
        if (name.isBlank()) {
            throw SaveTaskException.InvalidNameException
        }
        // In the case of a task creation also check for name duplication
        if (isTaskCreation && taskDao.countByName(name) > 0) {
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
    override suspend fun observeAll(category: Category?): Flow<List<Task>> {
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