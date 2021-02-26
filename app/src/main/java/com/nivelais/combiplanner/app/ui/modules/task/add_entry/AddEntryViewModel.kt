package com.nivelais.combiplanner.app.ui.modules.task.add_entry

import androidx.compose.runtime.mutableStateOf
import com.nivelais.combiplanner.app.ui.modules.main.GenericViewModel
import com.nivelais.combiplanner.domain.usecases.task.entry.CreateEntryParams
import com.nivelais.combiplanner.domain.usecases.task.entry.CreateEntryUseCase
import org.koin.core.scope.inject

/**
 * View model used to handle all the logic related to the management of our add entry button
 */
class AddEntryViewModel : GenericViewModel() {

    // Use case to add an entry to our task
    private val createEntryUseCase: CreateEntryUseCase by inject()

    /**
     * State indicating us if we display the advanced option possible for a new entry
     */
    val isAdvancedOptionsVisibleState = mutableStateOf(false)

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