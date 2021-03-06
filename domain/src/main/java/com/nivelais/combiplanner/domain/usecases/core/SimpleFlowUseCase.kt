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
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**+
 * Simple use case that return a flowable
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
