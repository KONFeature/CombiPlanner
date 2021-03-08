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
package com.nivelais.combiplanner.domain.repositories

import com.nivelais.combiplanner.domain.entities.Category
import com.nivelais.combiplanner.domain.exceptions.CreateCategoryException
import com.nivelais.combiplanner.domain.exceptions.DeleteCategoryException
import kotlinx.coroutines.flow.Flow

/**
 * Repository used to manage our category
 */
interface CategoryRepository {

    /**
     * Create a new category
     */
    @Throws(CreateCategoryException::class)
    suspend fun create(name: String, color: Long?): Category

    /**
     * Get all the category for a given
     */
    fun observeAll(): Flow<List<Category>>

    /**
     * Delete a category
     */
    @Throws(DeleteCategoryException::class)
    suspend fun delete(id: Long)

    /**
     * Migrate all the tasks associated to a given category to a new one
     */
    @Throws(DeleteCategoryException::class)
    suspend fun migrate(initialId: Long, targetId: Long?)
}
