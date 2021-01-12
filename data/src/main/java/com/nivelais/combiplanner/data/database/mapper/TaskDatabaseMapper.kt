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
    fun mapCategory(categoryEntity: ToOne<CategoryEntity>): Category =
        Category(
            id = categoryEntity.targetId,
            name = categoryEntity.target.name,
            color = categoryEntity.target.color
        )

}