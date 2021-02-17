package com.nivelais.combiplanner.app.ui.modules.task.entries

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import com.nivelais.combiplanner.app.ui.modules.main.GenericViewModel
import com.nivelais.combiplanner.domain.entities.TaskEntry
import com.nivelais.combiplanner.domain.usecases.task.entry.GetEntriesParams
import com.nivelais.combiplanner.domain.usecases.task.entry.GetEntriesResult
import com.nivelais.combiplanner.domain.usecases.task.entry.GetEntriesUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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

    // Access to the entries associated with our task
    val entriesState : StateFlow<GetEntriesResult>
        get() = getEntriesUseCase.stateFlow

    /**
     * The job that listen on the task entries
     */
    private var entriesListenerJob: Job? = null

    val doneEntries: SnapshotStateList<TaskEntry> = mutableStateListOf()
    val remainingEntries: SnapshotStateList<TaskEntry> = mutableStateListOf()

    /**
     * Listen to the task entries
     */
    fun listenToEntries(taskId: Long?) {
        // Setup the listener
        entriesListenerJob = viewModelScope.launch {
            getEntriesUseCase.stateFlow.collectLatest { getEntriesResult ->
                log.info(
                    "Received {} and {} entries to display",
                    getEntriesResult.remainingEntries.size,
                    getEntriesResult.doneEntries.size
                )

                // TODO : Cleaner way to do this (HashMap with id as param and then comparaison for perf ?)
                doneEntries.clear()
                doneEntries.addAll(getEntriesResult.doneEntries)
                remainingEntries.clear()
                remainingEntries.addAll(getEntriesResult.remainingEntries)
            }
        }.apply {
            invokeOnCompletion {
                log.info("Completion called because of", it)
            }
        }
        // Run the use case
        taskId?.let { getEntriesUseCase.run(GetEntriesParams(taskId = it)) }
    }

    /**
     * Cancel the listener on the task entries
     */
    fun disposeEntriesListener() {
        entriesListenerJob?.cancel()
    }

    override fun clearUseCases() {
        getEntriesUseCase.clear()
    }
}