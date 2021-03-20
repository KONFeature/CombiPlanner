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

import com.nivelais.combiplanner.data.database.dao.CategoryDao
import com.nivelais.combiplanner.data.database.entities.CategoryEntity
import com.nivelais.combiplanner.data.database.mapper.CategoryDatabaseMapper
import com.nivelais.combiplanner.domain.entities.Category
import com.nivelais.combiplanner.domain.exceptions.CreateCategoryException
import com.nivelais.combiplanner.domain.exceptions.DeleteCategoryException
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

    /**
     * @inheritDoc
     */
    @Throws(CreateCategoryException::class)
    override suspend fun create(name: String, color: Long?): Category {
        // Check if the name is valid or not
        if (name.isBlank()) {
            throw CreateCategoryException.InvalidNameException
        }

        // Check if we already got a category with this name or not
        if (categoryDao.countByName(name) > 0) {
            throw CreateCategoryException.DuplicateNameException
        }

        // Create and insert our entity, then return the mapped result
        val entity = CategoryEntity(name = name, color = color)
        categoryDao.save(entity)
        return categoryDatabaseMapper.entityToData(entity)
    }

    /**
     * @inheritDoc
     */
    override fun observeAll(): Flow<List<Category>> =
        categoryDao.getAll()
            .map { entities ->
                return@map categoryDatabaseMapper.entitiesToDatas(entities)
            }

    /**
     * @inheritDoc
     */
    @Throws(DeleteCategoryException::class)
    override suspend fun delete(id: Long) {
        // Get the category we need to delete
        val categoryEntity = categoryDao.get(id)
        if (categoryEntity == null) {
            // If we didn't find it exit directly
            return
        } else if (categoryEntity.tasks.isNotEmpty()) {
            // Throw a strategy required exception
            throw DeleteCategoryException.TaskAssociatedException
        }

        // Finally delete this category
        categoryDao.deleteById(id)
    }

    /**
     * @inheritDoc
     */
    @Throws(DeleteCategoryException::class)
    override suspend fun migrate(initialId: Long, targetId: Long?) {
        // If the two id are the same throw an exception
        if (initialId == targetId) throw DeleteCategoryException.MigrationIdInvalid
        // Fetch the initial and target category
        val initialCategory = categoryDao.get(initialId)
            ?: throw DeleteCategoryException.MigrationIdInvalid
        val targetCategory = targetId?.let { categoryDao.get(targetId) }
        // Migrate all the task to the other category
        initialCategory.tasks.forEach { task ->
            task.category.setAndPutTarget(targetCategory)
        }
    }
}
