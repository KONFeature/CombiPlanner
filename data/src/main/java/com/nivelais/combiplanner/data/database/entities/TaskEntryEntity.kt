package com.nivelais.combiplanner.data.database.entities

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

/**
 * Entry of one of our task in the database
 */
@Entity
data class TaskEntryEntity(
    @Id var id: Long = 0,
    var name: String? = null,
    var isDone: Boolean = false
)