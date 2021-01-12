package com.nivelais.combiplanner.domain.entities

/**
 * A category of task
 */
data class Category(
    var id: Long = 0,
    var name: String,
    var color: Long? = null,
)