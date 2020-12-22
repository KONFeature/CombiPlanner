package com.nivelais.combiplanner.data.database.entities

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany
import io.objectbox.relation.ToOne

/**
 * One of our task in the database
 */
@Entity
data class TaskEntity(
    @Id var id: Long = 0,
    var name: String = "Unknown",
    var description: String? = null,
) {
    lateinit var category: ToOne<CategoryEntity>

    lateinit var entries: ToMany<TaskEntryEntity>
}