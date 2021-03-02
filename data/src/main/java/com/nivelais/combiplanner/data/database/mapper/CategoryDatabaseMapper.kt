package com.nivelais.combiplanner.data.database.mapper

import com.nivelais.combiplanner.data.database.entities.CategoryEntity
import com.nivelais.combiplanner.domain.entities.Category
import org.mapstruct.InjectionStrategy
import org.mapstruct.Mapper

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR)
interface CategoryDatabaseMapper {

    fun entitiesToDatas(
        categoryEntities: List<CategoryEntity>
    ): List<Category>

    fun entityToData(
        categoryEntity: CategoryEntity
    ): Category
}