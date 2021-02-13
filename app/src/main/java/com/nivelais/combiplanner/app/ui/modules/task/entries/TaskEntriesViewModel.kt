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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
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

    override fun clearUseCases() {
        getEntriesUseCase.clear()
    }
}