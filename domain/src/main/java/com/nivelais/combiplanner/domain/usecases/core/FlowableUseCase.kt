/*
 * Copyright 2020-2021 Quentin Nivelais
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nivelais.combiplanner.domain.usecases.core

import com.nivelais.combiplanner.domain.common.logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
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
