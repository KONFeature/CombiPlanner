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
package com.nivelais.combiplanner.app.ui.modules.settings

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.nivelais.combiplanner.app.ui.modules.main.GenericViewModel
import com.nivelais.combiplanner.domain.entities.Category
import com.nivelais.combiplanner.domain.usecases.category.GetCategoriesUseCase
import kotlinx.coroutines.Job
import org.koin.core.scope.inject

class SettingsViewModel : GenericViewModel() {

    // Use case to list all the categories
    private val getCategoriesUseCase: GetCategoriesUseCase by inject()

    /**
     * The list of categories displayed in our view
     */
    val categories: SnapshotStateList<Category> = SnapshotStateList()

    /**
     * Job that listen on our categories
     */
    private var categoriesListenerJob: Job? = null

    init {
        // Load all the categories
        categoriesListenerJob = getCategoriesUseCase.observe(Unit) {
            categories.clear()
            categories.addAll(it)
        }
    }

    override fun clearUseCases() {
        categoriesListenerJob?.cancel()
    }
}
