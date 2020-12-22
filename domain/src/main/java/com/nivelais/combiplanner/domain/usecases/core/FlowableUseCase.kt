package com.nivelais.combiplanner.domain.usecases.core

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**+
 * Default use case
 * TODO : Improve that, surely a better way to handle state flow and a coroutine scoped execute function
 */
abstract class UseCase<in Params, Result> {

    /**
     * The inter state flow the child use case will update
     */
    protected val _stateFlow: MutableStateFlow<Result?> = MutableStateFlow(null)

    val stateFlow: StateFlow<Result?>
        get() = _stateFlow

    /**
     * Execute the use case
     */
    abstract suspend fun execute(params: Params)
}

/**
 * Class representing non value returned as use case result
 */
class None