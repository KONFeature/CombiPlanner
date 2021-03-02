package com.nivelais.combiplanner.data.repositories

import com.nivelais.combiplanner.data.database.dao.PictureDao
import com.nivelais.combiplanner.data.database.entities.PictureEntity
import com.nivelais.combiplanner.data.database.mapper.PictureDatabaseMapper
import com.nivelais.combiplanner.domain.entities.Picture
import com.nivelais.combiplanner.domain.repositories.PictureRepository
import org.mapstruct.factory.Mappers
import java.io.File
import java.time.Instant

/**
 * @inheritDoc
 */
class PictureRepositoryImpl(
    private val pictureDao: PictureDao,
    private val storageDirectory: File,
    private val pictureDatabaseMapper: PictureDatabaseMapper = Mappers.getMapper(
        PictureDatabaseMapper::class.java
    )
) : PictureRepository {

    override suspend fun create(bitmapBytes: ByteArray, type: String): Picture {
        // Check if the storage directory exist, else create it
        if(!storageDirectory.exists()) storageDirectory.mkdirs()

        // Create the file we will use to write our picture
        val pictureFile = File(storageDirectory, "combi_planner_pic_${Instant.now()}.$type")

        // Write to the file our picture
        pictureFile.writeBytes(bitmapBytes)

        // Create the entity that represent our file
        val entity = PictureEntity(path = pictureFile.absolutePath)
        pictureDao.save(entity)

        // Return the mapped entity
        return pictureDatabaseMapper.entityToData(entity)
    }


}