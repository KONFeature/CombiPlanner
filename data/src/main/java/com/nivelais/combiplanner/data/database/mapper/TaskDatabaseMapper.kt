package com.nivelais.combiplanner.data.database.mapper

import com.nivelais.combiplanner.data.database.entities.TaskEntity
import com.nivelais.combiplanner.domain.entities.Task
import org.mapstruct.InjectionStrategy
import org.mapstruct.Mapper

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR)
interface TaskDatabaseMapper {

    fun entityToData(
        taskEntity: TaskEntity
    ): Task

    fun entitiesToDatas(
        taskEntities: List<TaskEntity>
    ): List<Task>
}