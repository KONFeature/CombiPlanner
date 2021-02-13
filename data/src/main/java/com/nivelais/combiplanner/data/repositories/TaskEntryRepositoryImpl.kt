package com.nivelais.combiplanner.data.repositories

import com.nivelais.combiplanner.data.database.dao.TaskDao
import com.nivelais.combiplanner.data.database.dao.TaskEntryDao
import com.nivelais.combiplanner.data.database.entities.TaskEntryEntity
import com.nivelais.combiplanner.data.database.mapper.TaskEntryDatabaseMapper
import com.nivelais.combiplanner.domain.entities.Task
import com.nivelais.combiplanner.domain.entities.TaskEntry
import com.nivelais.combiplanner.domain.repositories.TaskEntryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mapstruct.factory.Mappers

/**
 * @inheritDoc
 */
class TaskEntryRepositoryImpl(
    private val taskDao: TaskDao,
    private val taskEntryDao: TaskEntryDao,
    private val taskEntryDatabaseMapper: TaskEntryDatabaseMapper = Mappers.getMapper(
        TaskEntryDatabaseMapper::class.java
    )
) : TaskEntryRepository {

    /**
     * @inheritDoc
     */
    override suspend fun add(
        taskId: Long,
        name: String,
        isDone: Boolean,
        taskDependencies: List<Task>
    ): TaskEntry {
        // Create the new task entry and save it
        val entity = TaskEntryEntity(
            name = name,
            isDone = isDone
        )
        taskEntryDao.save(entity)

        // TODO : Handle the task dependencies

        // Link it to our task
        taskDao.get(taskId)?.apply {
            entries.add(entity)
            entries.applyChangesToDb()
        }

        // Return our fresh entity
        return taskEntryDatabaseMapper.entityToData(entity)
    }

    /**
     * @inheritDoc
     */
    override suspend fun update(
        id: Long,
        name: String?,
        isDone: Boolean?,
        taskDependencies: List<Task>?
    ) {
        // Find the task entry
        taskEntryDao.get(id = id)?.let { taskEntry ->
            // Update the field we received
            name?.let { taskEntry.name = it }
            isDone?.let { taskEntry.isDone = it }

            // TODO : Handle task dependencies update

            // Save our updated entry
            taskEntryDao.save(taskEntry)
        }
    }

    /**
     * @inheritDoc
     */
    override suspend fun delete(id: Long) {
        TODO("Not yet implemented")
    }

    /**
     * @inheritDoc
     */
    override suspend fun observeForTask(taskId: Long): Flow<List<TaskEntry>> =
        taskEntryDao.observeForTask(taskId = taskId).map { entities ->
            taskEntryDatabaseMapper.entitiesToDatas(entities)
        }
}