package com.nivelais.combiplanner.domain.usecases.core

import com.nivelais.combiplanner.domain.common.logger
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlin.coroutines.CoroutineContext

/**+
 * Simple use case that return a flowable
 * TODO : Improve that, surely a better way to handle state flow and a coroutine scoped execute function
 */
abstract class FlowableUseCase<in Params, Result> : CoroutineScope {

    // Logger for all of our use cases
    val log by logger

    /**
     * The coroutine scope we will use to compute this use case
     */
    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.IO

    /**
     * The coroutine scope we will use for the state flow
     */
    abstract val observingScope: CoroutineScope;

    /**
     * The inter state flow the child use case will update
     */
    protected val resultFlow: MutableSharedFlow<Result> = MutableSharedFlow(
        replay = 1,
        extraBufferCapacity = 2,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    /**
     * Convert the result flow to a state flow listenable by the view model
     */
    val stateFlow: StateFlow<Result>
        get() = resultFlow.stateIn(observingScope, SharingStarted.WhileSubscribed(), initialValue())

    /**
     * Get the initial value when this use case is launched
     */
    abstract fun initialValue(): Result

    /**
     * Execute the use case
     */
    abstract suspend fun execute(params: Params)

    /**
     * Launch this use case from the scope provided as param
     */
    fun run(params: Params) {
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

// TODO : Handle simple computation use case (without state flow maybe) ??