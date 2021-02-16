package com.nivelais.combiplanner.app.ui.modules.task.add_entry

import com.nivelais.combiplanner.app.ui.modules.main.GenericViewModel
import com.nivelais.combiplanner.domain.usecases.task.entry.CreateEntryParams
import com.nivelais.combiplanner.domain.usecases.task.entry.CreateEntryUseCase
import org.koin.core.scope.inject

/**
 * View model used to handle all the logic related to the management of our task entries
 * TODO : Prevent rebuilding each time
 * TODO : Koin injection for this view model ? parameter injection ?
 * TODO : How to fetch the updated list of entries from the parent view model ?
 */
class AddEntryViewModel : GenericViewModel() {

    // Use case to add an entry to our task
    private val createEntryUseCase: CreateEntryUseCase by inject()

    /**
     * Add an entry to our task
     */
    fun addEntry(taskId: Long) {
        createEntryUseCase.run(CreateEntryParams(taskId = taskId, name = ""))
    }

    override fun clearUseCases() {
        createEntryUseCase.clear()
    }
}