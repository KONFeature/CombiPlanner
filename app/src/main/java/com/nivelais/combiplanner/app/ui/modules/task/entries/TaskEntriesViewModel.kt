package com.nivelais.combiplanner.app.ui.modules.task.entries

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import com.nivelais.combiplanner.app.ui.modules.main.GenericViewModel
import com.nivelais.combiplanner.domain.entities.TaskEntry

/**
 * View model used to handle all the logic related to the management of our task entries
 * TODO : Prevent rebuilding each time
 * TODO : Koin injection for this view model ? parameter injection ?
 * TODO : How to fetch the updated list of entries from the parent view model ?
 */
class TaskEntriesViewModel(
    entries: List<TaskEntry>,
    private val onEntriesUpdated: (List<TaskEntry>) -> Unit
) : GenericViewModel() {

    /**
     * All the entries in this view
     */
    private val entriesState: SnapshotStateList<TaskEntryState>

    /**
     * List containing all of our entries
     */
    val remainingEntriesState: SnapshotStateList<TaskEntryState>
        get() = entriesState.filter { !it.isDoneState.value }.toMutableStateList()

    /**
     * List containing the entries already done
     */
    val doneEntriesState: SnapshotStateList<TaskEntryState>
        get() = entriesState.filter { it.isDoneState.value }.toMutableStateList()

    /**
     * The entries mapped
     */
    private val entries: List<TaskEntry>
        get() = entriesState.map { entryState ->
            TaskEntry(
                id = entryState.initialId,
                name = entryState.nameState.value,
                isDone = entryState.isDoneState.value
            )
        }

    init {
        entriesState = entries.map { taskEntry ->
            TaskEntryState(
                initialId = taskEntry.id,
                nameState = mutableStateOf(taskEntry.name),
                isDoneState = mutableStateOf(taskEntry.isDone)
            )
        }.toMutableStateList()
    }

    /**
     * Add a new entry in our list
     */
    fun addEntry() {
        entriesState.add(
            TaskEntryState(
                initialId = 0L,
                nameState = mutableStateOf(""),
                isDoneState = mutableStateOf(false)
            )
        )
        entriesUpdated()
    }

    /**
     * Delete an entry at a specified index
     */
    fun deleteEntry(entry: TaskEntryState) {
        entriesState.remove(entry)
        entriesUpdated()
    }

    /**
     * Function called when the entries are updated
     */
    fun entriesUpdated() = onEntriesUpdated(entries)

    /**
     * Get the updated task list
     */
    fun getUpdatedEntries(): List<TaskEntry> =
        entriesState.map { entryState ->
            TaskEntry(
                id = entryState.initialId,
                name = entryState.nameState.value,
                isDone = entryState.isDoneState.value
            )
        }

    /**
     * Class containing the state for each one of our task entries
     */
    data class TaskEntryState(
        val initialId: Long,
        val nameState: MutableState<String>,
        val isDoneState: MutableState<Boolean>
    )
}