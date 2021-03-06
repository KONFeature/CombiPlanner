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

import com.nivelais.combiplanner.data.database.entities.TaskEntryEntity
import com.nivelais.combiplanner.domain.entities.TaskEntry
import org.mapstruct.InjectionStrategy
import org.mapstruct.Mapper

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR)
interface TaskEntryDatabaseMapper {

    fun entityToData(
        taskEntryEntity: TaskEntryEntity
    ): TaskEntry

    fun entitiesToDatas(
        taskEntryEntities: List<TaskEntryEntity>
    ): List<TaskEntry>
}
