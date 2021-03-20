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
package com.nivelais.combiplanner.app.ui.modules.task.entry

import androidx.compose.runtime.mutableStateOf
import com.nivelais.combiplanner.app.ui.modules.main.GenericViewModel
import com.nivelais.combiplanner.domain.entities.TaskEntry
import com.nivelais.combiplanner.domain.usecases.task.entry.DeleteEntryParams
import com.nivelais.combiplanner.domain.usecases.task.entry.DeleteEntryUseCase
import com.nivelais.combiplanner.domain.usecases.task.entry.UpdateEntryParams
import com.nivelais.combiplanner.domain.usecases.task.entry.UpdateEntryUseCase
import org.koin.core.component.inject

/**
 * View model used to handle all the logic related to the management of our task entries
 */
class TaskEntryViewModel : GenericViewModel() {

    private var taskEntry: TaskEntry? = null

    // Use cases to create, update or delete our task
    private val updateEntryUseCase: UpdateEntryUseCase by inject()
    private val deleteEntryUseCase: DeleteEntryUseCase by inject()

    // The element of the view
    val nameState = mutableStateOf(taskEntry?.name ?: "")
    val isDoneState = mutableStateOf(taskEntry?.isDone ?: false)

    /**
     * Update the current displayed entry
     */
    fun updateEntry(newEntry: TaskEntry?) {
        // Update the displayed entry
        taskEntry = newEntry
        // Reset the field
        nameState.value = newEntry?.name ?: "null"
        isDoneState.value = newEntry?.isDone ?: false
    }

    /**
     * Delete the current entry
     */
    fun onDeleteClicked() {
        taskEntry?.let {
            deleteEntryUseCase.run(
                DeleteEntryParams(entryId = it.id)
            )
        }
    }

    /**
     * When the name of the task entry is changed
     */
    fun onNameChange(name: String) {
        nameState.value = name
        taskEntry?.let {
            updateEntryUseCase.run(
                UpdateEntryParams(
                    entryId = it.id,
                    name = name
                )
            )
        }
    }

    /**
     * When the is done state of the entry is changed
     */
    fun onIsDoneChanged(isDone: Boolean) {
        isDoneState.value = isDone
        taskEntry?.let {
            updateEntryUseCase.run(
                UpdateEntryParams(
                    entryId = it.id,
                    isDone = isDone
                )
            )
        }
    }

    override fun clearUseCases() {
        updateEntryUseCase.clear()
        deleteEntryUseCase.clear()
    }
}
