package com.nivelais.combiplanner.domain.entities

/**
 * One of our task
 */
data class Task(
    var id: Long = 0,
    var name: String,
    var description: String? = null,
    val category: Category,
    val entries: List<TaskEntry> = emptyList()
)