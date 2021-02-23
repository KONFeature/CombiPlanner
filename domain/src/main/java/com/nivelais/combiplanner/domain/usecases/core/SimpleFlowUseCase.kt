package com.nivelais.combiplanner.domain.usecases.core

import com.nivelais.combiplanner.domain.common.logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

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
    abstract fun execute(params: Params): Flow<Result>

    /**
     * Directly observe the result of this use case
     */
    fun observe(params: Params, action: suspend (value: Result) -> Unit): Job =
        observingScope.launch {
            log.debug("Observing this use case with the computation scope")
            execute(params).collect(action)
        }
}