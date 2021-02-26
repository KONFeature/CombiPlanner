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
        // Create our new entity
        val taskEntry = TaskEntryEntity(
            name = name,
            isDone = isDone
        )

        // Find the task we need to associated it with
        taskDao.get(taskId)?.apply {
            // If we got the task, run all the db in a transaction
            taskEntryDao.boxStore.runInTx {
                // Persist the task entry
                taskEntryDao.save(taskEntry)

                // Link it to the task
                entries.add(taskEntry)
                entries.applyChangesToDb()
            }
        }

        // TODO : Handle the task dependencies

        // Return our fresh entity
        return taskEntryDatabaseMapper.entityToData(taskEntry)
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
        taskEntryDao.delete(id)
    }

    /**
     * @inheritDoc
     */
    override fun observeForTask(taskId: Long): Flow<List<TaskEntry>> =
        taskEntryDao.observeForTask(taskId = taskId).map { entities ->
            taskEntryDatabaseMapper.entitiesToDatas(entities)
        }
}