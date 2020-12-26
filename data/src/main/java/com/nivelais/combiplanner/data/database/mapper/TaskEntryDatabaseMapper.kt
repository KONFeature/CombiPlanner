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

    fun dataToEntity(
        taskEntry: TaskEntry
    ): TaskEntryEntity

    fun datasToEntities(
        taskEntries: List<TaskEntry>
    ): List<TaskEntryEntity>
}