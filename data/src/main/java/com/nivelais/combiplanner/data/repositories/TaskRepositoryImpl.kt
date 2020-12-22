package com.nivelais.combiplanner.data.repositories

import com.nivelais.combiplanner.data.database.dao.TaskDao
import com.nivelais.combiplanner.data.database.entities.TaskEntity
import com.nivelais.combiplanner.data.database.mapper.TaskDatabaseMapper
import com.nivelais.combiplanner.domain.entities.Category
import com.nivelais.combiplanner.domain.entities.Task
import com.nivelais.combiplanner.domain.repositories.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mapstruct.factory.Mappers

/**
 * @inheritDoc
 */
class TaskRepositoryImpl(
    private val taskDao: TaskDao,
    private val taskDatabaseMapper: TaskDatabaseMapper = Mappers.getMapper(
        TaskDatabaseMapper::class.java
    )
) : TaskRepository {

    /**
     * @inheritDoc
     */
    override suspend fun create(name: String, description: String?, category: Category): Task {
        // Create the task entity
        val entity = TaskEntity(name = name, description = description)
        entity.category.targetId = category.id

        // Insert it in the database and return the mapped result
        val insertedEntity = taskDao.save(entity)
        return taskDatabaseMapper.entityToData(insertedEntity)
    }

    /**
     * @inheritDoc
     */
    override suspend fun observeAll(category: Category?): Flow<List<Task>> {
        // Get the right flow
        val entitiesFlow = category?.let {
            taskDao.getForCategory(it.id)
        } ?: run {
            taskDao.getAll()
        }

        // Return the mapped entities flow
        return entitiesFlow.map { entities ->
            taskDatabaseMapper.entitiesToDatas(entities)
        }
    }
}