package com.nivelais.combiplanner.data.database.mapper

import com.nivelais.combiplanner.data.database.entities.PictureEntity
import com.nivelais.combiplanner.data.database.entities.TaskEntryEntity
import com.nivelais.combiplanner.domain.entities.Picture
import com.nivelais.combiplanner.domain.entities.TaskEntry
import org.mapstruct.InjectionStrategy
import org.mapstruct.Mapper

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR)
interface PictureDatabaseMapper {

    fun entityToData(
        pictureEntity: PictureEntity
    ): Picture

    fun entitiesToDatas(
        pictureEntities: List<PictureEntity>
    ): List<Picture>
}