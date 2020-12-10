package com.nivelais.combiplanner.domain.usecases

import com.nivelais.combiplanner.domain.common.logger
import com.nivelais.combiplanner.domain.repositories.TestRepository
import com.nivelais.combiplanner.domain.usecases.core.None
import com.nivelais.combiplanner.domain.usecases.core.UseCase
import kotlinx.coroutines.delay
import kotlin.time.ExperimentalTime
import kotlin.time.seconds

/**
 * Simple test use case that emit test value to a StateFlow
 */
class TestUseCase(
    private val testRepository: TestRepository
) : UseCase<None, String>() {

    companion object {
        private val log by logger
    }

    @OptIn(ExperimentalTime::class)
    override suspend fun execute(params: None) {
        _stateFlow.emit("Starting");
        log.error("Starting the test use case with {} and {}", params, "test")
        delay(1.seconds)
        repeat(5) {
            val generatedString = testRepository.generateString()
            _stateFlow.emit(generatedString);
            delay(1.seconds)
        }
        delay(1.seconds)
        _stateFlow.emit("Finish");
    }

}