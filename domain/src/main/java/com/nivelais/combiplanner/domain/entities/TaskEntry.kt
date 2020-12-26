package com.nivelais.combiplanner.domain.entities

/**
 * Entry of one of our task
 */
data class TaskEntry(
    var id: Long = 0,
    var name: String,
    var isDone: Boolean = false
)