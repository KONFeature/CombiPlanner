package com.nivelais.combiplanner.domain.usecases.core

import com.nivelais.combiplanner.domain.common.logger
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlin.coroutines.CoroutineContext

/**+
 * Simple use case that return a flowable
 */
abstract class FlowableUseCase<in Param, Result> : CoroutineScope {

    // Logger for all of our use cases
    val log by logger

    /**
     * The dispatcher that will be used for our use case
     */
    protected open val dispatcher = Dispatchers.IO

    /**
     * The coroutine scope we will use to compute this use case
     */
    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + dispatcher

    /**
     * The inter state flow the child use case will update
     */
    protected val resultFlow: MutableSharedFlow<Result> = MutableSharedFlow(
        replay = 1,
        extraBufferCapacity = 3,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    /**
     * Result flow in read only mode for accessor
     */
    val flow: Flow<Result>
        get() = resultFlow.asSharedFlow()

    /**
     * Execute the use case
     */
    protected abstract suspend fun execute(params: Param)

    /**
     * Launch this use case from the scope provided as param
     */
    fun run(params: Param) {
        log.debug("Launching this use case with the computation scope")
        launch {
            execute(params)
        }
    }

    /**
     * Clear this use case (close this scope)
     */
    fun clear() {
        log.info("Clearing this use case and all the associated job")
        cancel()
    }
}