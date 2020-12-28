package com.nivelais.combiplanner.app.ui.modules.task.entries

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import com.nivelais.combiplanner.app.ui.modules.main.GenericViewModel
import com.nivelais.combiplanner.domain.entities.TaskEntry

/**
 * View model used to handle all the logic related to the management of our task entries
 */
class TaskEntriesViewModel(entries: List<TaskEntry>) : GenericViewModel() {

    /**
     * List containing all of our entries
     */
    val entriesState: SnapshotStateList<TaskEntryState>

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
    fun addEntry() = entriesState.add(
        TaskEntryState(
            initialId = 0L,
            nameState = mutableStateOf(""),
            isDoneState = mutableStateOf(false)
        )
    )

    /**
     * Delete an entry at a specified index
     */
    fun deleteEntry(index: Int) = entriesState.removeAt(index = index)

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