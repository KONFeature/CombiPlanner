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
) : SimpleFlowUseCase<GetEntriesParams, GetEntriesResult>() {

    override fun execute(params: GetEntriesParams): Flow<GetEntriesResult> {
        log.debug("Listening to the entries for the task with id {}", params.taskId)
        return taskEntryRepository.observeForTask(params.taskId).map { allEntries ->
            GetEntriesResult(
                remainingEntries = allEntries.filter { !it.isDone },
                doneEntries = allEntries.filter { it.isDone }
            )
        }
    }

}

/**
 * Input for our use case
 */
data class GetEntriesParams(
    val taskId: Long
)

/**
 * Input for our use case
 */
data class GetEntriesResult(
    val remainingEntries: List<TaskEntry>,
    val doneEntries: List<TaskEntry>,
)