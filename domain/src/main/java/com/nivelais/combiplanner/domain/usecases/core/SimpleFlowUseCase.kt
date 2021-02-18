package com.nivelais.combiplanner.domain.usecases.core

import com.nivelais.combiplanner.domain.common.logger
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlin.coroutines.CoroutineContext

/**+
 * Simple use case that return a flowable
 * TODO : Improve that, surely a better way to handle state flow and a coroutine scoped execute function
 */
abstract class SimpleFlowUseCase<in Params, Result> {

    // Logger for all of our use cases
    val log by logger

    /**
     * The coroutine scope we will use for the state flow
     */
    abstract val observingScope: CoroutineScope

    /**
     * Execute the use case
     */
    abstract fun execute(params: Params) : Flow<Result>

    /**
     * Launch this use case from the scope provided as param
     */
    suspend fun launch(params: Params): StateFlow<Result> {
        log.debug("Launching this use case with the computation scope")
        return execute(params).stateIn(observingScope)
    }
}