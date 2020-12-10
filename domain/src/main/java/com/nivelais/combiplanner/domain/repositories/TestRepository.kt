package com.nivelais.combiplanner.domain.repositories

/**
 * Sample test repository
 */
interface TestRepository {

    /**
     * Used to generate a String
     */
    suspend fun generateString(): String

}