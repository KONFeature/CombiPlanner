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
package com.nivelais.combiplanner.app.ui.modules.task.add_entry

import androidx.lifecycle.viewModelScope
import com.nivelais.combiplanner.app.ui.modules.main.GenericViewModel
import com.nivelais.combiplanner.domain.usecases.task.entry.AddEntryParams
import com.nivelais.combiplanner.domain.usecases.task.entry.AddEntryUseCase
import kotlinx.coroutines.launch
import org.koin.core.component.inject

/**
 * View model used to handle all the logic related to the management of our add entry button
 */
class AddEntryViewModel : GenericViewModel() {

    // Use case to add an entry to our task
    private val addEntryUseCase: AddEntryUseCase by inject()

    /**
     * Add an entry to our task
     */
    fun addEntry(taskId: Long) {
        viewModelScope.launch {
            // Send all of that to the use case
            addEntryUseCase.run(AddEntryParams(taskId = taskId))
        }
    }

    override fun clearUseCases() {
        addEntryUseCase.clear()
    }
}
