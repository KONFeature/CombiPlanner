package com.nivelais.combiplanner.data.repositories

import com.nivelais.combiplanner.domain.repositories.TestRepository
import kotlin.random.Random

class TestRepositoryImpl : TestRepository {

    override suspend fun generateString(): String {
        return "Test ${Random.nextInt(0, 9)}"
    }

}