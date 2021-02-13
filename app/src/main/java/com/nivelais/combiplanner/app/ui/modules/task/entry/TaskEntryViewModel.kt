package com.nivelais.combiplanner.app.ui.modules.task.entry

import androidx.compose.runtime.mutableStateOf
import com.nivelais.combiplanner.app.ui.modules.main.GenericViewModel
import com.nivelais.combiplanner.domain.entities.TaskEntry
import com.nivelais.combiplanner.domain.usecases.task.entry.*
import org.koin.core.scope.inject

/**
 * View model used to handle all the logic related to the management of our task entries
 * TODO : Prevent rebuilding each time
 * TODO : Koin injection for this view model ? parameter injection ?
 * TODO : How to fetch the updated list of entries from the parent view model ?
 */
class TaskEntryViewModel(
    private val taskEntry: TaskEntry
) : GenericViewModel() {

    // Use cases to create, update or delete our task
    private val createEntryUseCase: CreateEntryUseCase by inject()
    private val updateEntryUseCase: UpdateEntryUseCase by inject()
    private val deleteEntryUseCase: DeleteEntryUseCase by inject()

    // The element of the view
    val nameState = mutableStateOf(taskEntry.name)
    val isDoneState = mutableStateOf(taskEntry.isDone)

    /**
     * Delete an entry at a specified index
     */
    fun onDeleteClicked() {
        deleteEntryUseCase.run(DeleteEntryParams(entryId = taskEntry.id))
    }

    fun onNameChange(name: String) {
        nameState.value = name
        updateEntryUseCase.run(
            UpdateEntryParams(
                entryId = taskEntry.id,
                name = name
            )
        )
    }

    fun onIsDoneChanged(isDone: Boolean) {
        isDoneState.value = isDone
        updateEntryUseCase.run(
            UpdateEntryParams(
                entryId = taskEntry.id,
                isDone = isDone
            )
        )
    }

    fun updateEntry() {

    }

    override fun clearUseCases() {
        createEntryUseCase.clear()
        updateEntryUseCase.clear()
        deleteEntryUseCase.clear()
    }
}