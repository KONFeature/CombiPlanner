package com.nivelais.combiplanner.app.ui.modules.task.entries

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.nivelais.combiplanner.app.ui.modules.main.GenericViewModel
import com.nivelais.combiplanner.domain.entities.TaskEntry
import com.nivelais.combiplanner.domain.usecases.task.entry.GetEntriesParams
import com.nivelais.combiplanner.domain.usecases.task.entry.GetEntriesUseCase
import kotlinx.coroutines.Job
import org.koin.core.scope.inject

/**
 * View model used to handle all the logic related to the management of our task entries
 * TODO : Prevent rebuilding each time
 * TODO : Koin injection for this view model ? parameter injection ?
 * TODO : How to fetch the updated list of entries from the parent view model ?
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
            if(newEntries.none { it.id == oldEntry.id }) {
                oldEntriesIterator.remove()
            }
        }

        // Add the new entries
        newEntries.forEach { potentialNewEntry ->
            if(none { it.id == potentialNewEntry.id }) {
                add(potentialNewEntry)
            }
        }
    }
}