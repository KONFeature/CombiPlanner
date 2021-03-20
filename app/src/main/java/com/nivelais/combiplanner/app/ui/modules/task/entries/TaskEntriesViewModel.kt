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
package com.nivelais.combiplanner.app.ui.modules.task.entries

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.nivelais.combiplanner.app.ui.modules.main.GenericViewModel
import com.nivelais.combiplanner.domain.entities.TaskEntry
import com.nivelais.combiplanner.domain.usecases.task.entry.GetEntriesParams
import com.nivelais.combiplanner.domain.usecases.task.entry.GetEntriesUseCase
import kotlinx.coroutines.Job
import org.koin.core.component.inject

/**
 * View model used to handle all the logic related to the management of our task entries
 */
class TaskEntriesViewModel : GenericViewModel() {

    // Use case to get our entities from the entry id
    private val getEntriesUseCase: GetEntriesUseCase by inject()

    /**
     * The job that listen on the task entries
     */
    private var entriesListenerJob: Job? = null

    val entries: SnapshotStateList<TaskEntry> = mutableStateListOf()

    /**
     * Listen to the task entries
     */
    fun listenToEntries(taskId: Long?) {
        if (taskId == null) return

        // Setup the listener
        entriesListenerJob =
            getEntriesUseCase.observe(GetEntriesParams(taskId = taskId)) { newEntries ->
                log.info(
                    "Received {} entries to display",
                    newEntries.size,
                )
                entries.updateFrom(newEntries)
            }
    }

    /**
     * Cancel the listener on the task entries
     */
    fun disposeEntriesListener() {
        entriesListenerJob?.cancel()
    }

    override fun clearUseCases() {
        entriesListenerJob?.cancel()
    }

    /**
     * Update a snapshot state list of entries from a single list
     */
    private fun SnapshotStateList<TaskEntry>.updateFrom(newEntries: List<TaskEntry>) {
        // Remove entry not present anymore
        val oldEntriesIterator = iterator()
        while (oldEntriesIterator.hasNext()) {
            val oldEntry = oldEntriesIterator.next()
            if (newEntries.none { it.id == oldEntry.id }) {
                oldEntriesIterator.remove()
            }
        }

        // Add the new entries
        newEntries.forEach { potentialNewEntry ->
            if (none { it.id == potentialNewEntry.id }) {
                add(potentialNewEntry)
            }
        }
    }
}
