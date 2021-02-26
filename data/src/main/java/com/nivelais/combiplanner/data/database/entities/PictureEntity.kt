package com.nivelais.combiplanner.data.database.entities

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany
import io.objectbox.relation.ToOne

/**
 * One of our picture in the database
 */
@Entity
data class PictureEntity(
    @Id var id: Long = 0,
    var path: String? = null,
)