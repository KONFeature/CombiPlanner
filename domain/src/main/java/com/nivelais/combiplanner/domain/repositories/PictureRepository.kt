package com.nivelais.combiplanner.domain.repositories

import com.nivelais.combiplanner.domain.entities.Category
import com.nivelais.combiplanner.domain.entities.Picture
import com.nivelais.combiplanner.domain.entities.Task
import com.nivelais.combiplanner.domain.exceptions.SaveTaskException
import kotlinx.coroutines.flow.Flow

/**
 * Repository used to manage our picture
 */
interface PictureRepository {

    /**
     * Create a new picture from a bitmap
     */
    suspend fun create(
        bitmapBytes: ByteArray,
        type: String,
    ): Picture
}