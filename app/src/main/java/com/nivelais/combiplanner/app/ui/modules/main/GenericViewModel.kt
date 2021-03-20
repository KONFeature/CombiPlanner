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
package com.nivelais.combiplanner.app.ui.modules.main

import androidx.lifecycle.ViewModel
import com.nivelais.combiplanner.domain.common.logger
import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.createScope
import org.koin.core.component.newScope
import org.koin.core.scope.Scope

/**
 * Generic view model bind to a Koin Scope
 */
open class GenericViewModel : ViewModel(), KoinScopeComponent {

    // Logger for all of our view model
    val log by logger

    // The scope for this view model
    override val scope: Scope by lazy { createScope(this) }

    // Abstract function that will close all the use case
    open fun clearUseCases() {}

    /**
     * When view model cleared close the scope
     */
    override fun onCleared() {
        log.info("Clearing this view model, and so the use case and the koin scope")
        super.onCleared()
        clearUseCases()
        closeScope()
    }
}
