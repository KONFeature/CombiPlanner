package com.nivelais.combiplanner.app.ui.modules.task.entries

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import com.nivelais.combiplanner.app.ui.modules.main.GenericViewModel
import com.nivelais.combiplanner.domain.entities.TaskEntry
import com.nivelais.combiplanner.domain.usecases.task.DeleteTaskUseCase
import com.nivelais.combiplanner.domain.usecases.task.GetTaskUseCase
import com.nivelais.combiplanner.domain.usecases.task.SaveTaskUseCase
import com.nivelais.combiplanner.domain.usecases.task.entry.*
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.scope.inject

/**
 * View model used to handle all the logic related to the management of our task entries
 * TODO : Prevent rebuilding each time
 * TODO : Koin injection for this view model ? parameter injection ?
 * TODO : How to fetch the updated list of entries from the parent view model ?
 */
class TaskEntriesViewModel(
    taskId: Long?
) : GenericViewModel() {

    // Use case to get our entities from the entry id
    private val getEntriesUseCase: GetEntriesUseCase by inject()

    // Use cases to create, update or delete our task
    private val createEntryUseCase: CreateEntryUseCase by inject()
    private val updateEntryUseCase: UpdateEntryUseCase by inject()
    private val deleteEntryUseCase: DeleteEntryUseCase by inject()

    init {
        // When we launch this view load the entries for the given task id
        taskId?.let {
            // TODO : Handle no task id
            getEntriesUseCase.run(GetEntriesParams(taskId = it))
        }
    }

    /**
     * All the entries in this view
     */
    val entriesState: StateFlow<GetEntriesResult> = getEntriesUseCase.stateFlow

    /**
     * List containing all of our entries
     */
    val remainingEntriesState: SnapshotStateList<TaskEntry>
        get() = entriesState.value.remainingEntries.toMutableStateList()

    /**
     * List containing the entries already done
     */
    val doneEntriesState: SnapshotStateList<TaskEntry>
        get() = entriesState.value.doneEntries.toMutableStateList()

    /**
     * The entries mapped
     */
//    private val entries: List<TaskEntry>
//        get() = entriesState.map { entryState ->
//            TaskEntry(
//                id = entryState.initialId,
//                name = entryState.nameState.value,
//                isDone = entryState.isDoneState.value
//            )
//        }

    /**
     * Add a new entry in our list
     */
//    fun addEntry() {
//        entriesState.add(
//            TaskEntryState(
//                initialId = 0L,
//                nameState = mutableStateOf(""),
//                isDoneState = mutableStateOf(false)
//            )
//        )
//        entriesUpdated()
//    }

    /**
     * Delete an entry at a specified index
     */
    fun deleteEntry(entryId: Long) {
        deleteEntryUseCase.run(DeleteEntryParams(entryId = entryId))
    }

    /**
     * Function called when the entries are updated
     */
//    fun entriesUpdated() = onEntriesUpdated(entries)

    /**
     * Get the updated task list
     */
//    fun getUpdatedEntries(): List<TaskEntry> =
//        entriesState.map { entryState ->
//            TaskEntry(
//                id = entryState.initialId,
//                name = entryState.nameState.value,
//                isDone = entryState.isDoneState.value
//            )
//        }

    /**
     * Class containing the state for each one of our task entries
     */
    data class TaskEntryState(
        val initialId: Long,
        val nameState: MutableState<String>,
        val isDoneState: MutableState<Boolean>
    )
}