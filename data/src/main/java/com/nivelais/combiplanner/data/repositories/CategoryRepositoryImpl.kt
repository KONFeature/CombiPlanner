package com.nivelais.combiplanner.data.repositories

import com.nivelais.combiplanner.data.database.dao.CategoryDao
import com.nivelais.combiplanner.data.database.entities.CategoryEntity
import com.nivelais.combiplanner.data.database.mapper.CategoryDatabaseMapper
import com.nivelais.combiplanner.domain.common.logger
import com.nivelais.combiplanner.domain.entities.Category
import com.nivelais.combiplanner.domain.repositories.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mapstruct.factory.Mappers

/**
 * @inheritDoc
 */
class CategoryRepositoryImpl(
    private val categoryDao: CategoryDao,
    private val categoryDatabaseMapper: CategoryDatabaseMapper = Mappers.getMapper(
        CategoryDatabaseMapper::class.java
    )
) : CategoryRepository {

    val log by logger

    /**
     * @inheritDoc
     */
    override suspend fun create(name: String, color: Long?): Category {
        // Create and insert our entity, then return the mapped result
        val entity = CategoryEntity(name = name, color = color)
        categoryDao.save(entity)
        return categoryDatabaseMapper.entityToData(entity)
    }

    /**
     * @inheritDoc
     */
    override suspend fun observeAll(): Flow<List<Category>> =
        categoryDao.getAll()
            .map { entities ->
                return@map categoryDatabaseMapper.entitiesToDatas(entities)
            }

    /**
     * @inheritDoc
     */
    override suspend fun delete(id: Long) {
        categoryDao.deleteById(id)
    }
}