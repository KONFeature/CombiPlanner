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
package com.nivelais.combiplanner.data.database.mapper

import com.nivelais.combiplanner.data.database.entities.CategoryEntity
import com.nivelais.combiplanner.data.database.entities.TaskEntity
import com.nivelais.combiplanner.domain.entities.Category
import com.nivelais.combiplanner.domain.entities.Task
import io.objectbox.relation.ToOne
import org.mapstruct.InjectionStrategy
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import org.mapstruct.factory.Mappers

/**
 * Mapper for our task entity to pojo
 */
@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR)
abstract class TaskDatabaseMapper {

    abstract fun entitiesToDatas(
        taskEntities: List<TaskEntity>
    ): List<Task>

    @Mappings(
        Mapping(target = "category", expression = "java(mapCategory(taskEntity.category))")
    )
    abstract fun entityToData(
        taskEntity: TaskEntity
    ): Task

    /**
     * Map ToOne relations of our task
     */
    fun mapCategory(categoryEntity: ToOne<CategoryEntity>): Category? {
        // Return a category only if it's present
        return if (!categoryEntity.isNull) {
            Mappers.getMapper(CategoryDatabaseMapper::class.java)
                .entityToData(categoryEntity.target)
        } else {
            null
        }
    }
}
