package com.nivelais.combiplanner.app.ui.modules.task.add_entry

import androidx.lifecycle.viewModelScope
import com.nivelais.combiplanner.app.ui.modules.main.GenericViewModel
import com.nivelais.combiplanner.domain.usecases.task.entry.AddEntryParams
import com.nivelais.combiplanner.domain.usecases.task.entry.AddEntryUseCase
import kotlinx.coroutines.launch
import org.koin.core.scope.inject

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