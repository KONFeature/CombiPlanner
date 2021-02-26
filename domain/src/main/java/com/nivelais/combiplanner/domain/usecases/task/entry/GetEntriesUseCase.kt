package com.nivelais.combiplanner.domain.usecases.task.entry

import com.nivelais.combiplanner.domain.entities.TaskEntry
import com.nivelais.combiplanner.domain.repositories.TaskEntryRepository
import com.nivelais.combiplanner.domain.usecases.core.SimpleFlowUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Use case used to get all the entries of a given task from it's id
 */
class GetEntriesUseCase(
    override val observingScope: CoroutineScope,
    private val taskEntryRepository: TaskEntryRepository
) : SimpleFlowUseCase<GetEntriesParams, List<TaskEntry>>() {

    override fun execute(params: GetEntriesParams): Flow<List<TaskEntry>> {
        log.debug("Listening to the entries for the task with id {}", params.taskId)
        return taskEntryRepository.observeForTask(params.taskId)
            .map { entries -> entries.sortedBy { it.isDone } }
    }

}

/**
 * Input for our use case
 */
data class GetEntriesParams(
    val taskId: Long
)