package com.nivelais.combiplanner.data.database.entities

import io.objectbox.annotation.Backlink
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany
import io.objectbox.relation.ToOne

/**
 * Entry of one of our task in the database
 */
@Entity
data class TaskEntryEntity(
    @Id var id: Long = 0,
    var name: String? = null,
    var isDone: Boolean = false,
) {
    lateinit var pictures: ToMany<PictureEntity>
    lateinit var taskDependencies: ToMany<TaskEntity>
}