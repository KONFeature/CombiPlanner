package com.nivelais.combiplanner.data.database.mapper

import com.nivelais.combiplanner.data.database.entities.TaskEntity
import com.nivelais.combiplanner.data.database.entities.TaskEntryEntity
import com.nivelais.combiplanner.domain.entities.TaskEntry
import io.objectbox.relation.ToMany
import org.mapstruct.*
import org.mapstruct.factory.Mappers

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR)
interface TaskEntryDatabaseMapper {

    fun entityToData(
        taskEntryEntity: TaskEntryEntity
    ): TaskEntry

    fun entitiesToDatas(
        taskEntryEntities: List<TaskEntryEntity>
    ): List<TaskEntry>

}