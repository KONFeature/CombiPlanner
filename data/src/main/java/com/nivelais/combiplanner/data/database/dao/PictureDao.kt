package com.nivelais.combiplanner.data.database.dao

import com.nivelais.combiplanner.data.database.entities.PictureEntity
import io.objectbox.Box
import io.objectbox.BoxStore
import io.objectbox.kotlin.boxFor

/**
 * Dao used to manage our picture object
 */
class PictureDao(boxStore: BoxStore) {

    /**
     * Box used to access our task entries
     */
    private val box: Box<PictureEntity> = boxStore.boxFor()
}