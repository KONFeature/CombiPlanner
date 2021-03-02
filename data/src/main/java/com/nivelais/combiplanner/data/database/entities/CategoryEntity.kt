package com.nivelais.combiplanner.data.database.entities

import com.nivelais.combiplanner.domain.entities.Category
import io.objectbox.annotation.Backlink
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany
import io.objectbox.relation.ToOne

/**
 * A category of task in the database
 */
@Entity
data class CategoryEntity(
    @Id var id: Long = 0,
    var name: String = "Unknown",
    var color: Long?,
) {
    @Backlink(to = "category")
    lateinit var tasks: ToMany<TaskEntity>
}