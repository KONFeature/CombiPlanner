package com.nivelais.combiplanner.domain.usecases.task.entry

import com.nivelais.combiplanner.domain.entities.TaskEntry
import com.nivelais.combiplanner.domain.repositories.TaskEntryRepository
import com.nivelais.combiplanner.domain.usecases.core.FlowableUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map

/**
 * Use case used to get all the entries of a given task from it's id
 */
class GetEntriesUseCase(
    override val observingScope: CoroutineScope,
    private val taskEntryRepository: TaskEntryRepository
) : FlowableUseCase<GetEntriesParams, GetEntriesResult>() {

    @OptIn(FlowPreview::class)
    override suspend fun execute(params: GetEntriesParams) {
        log.debug("Listening to the entries for the task with id {}", params.taskId)
        val entriesFlow = taskEntryRepository.observeForTask(params.taskId)
        try {
            entriesFlow.collectLatest { allEntries ->
                resultFlow.emit(GetEntriesResult(
                    remainingEntries = allEntries.filter { !it.isDone },
                    doneEntries = allEntries.filter { it.isDone }
                ))
            }
        } catch(exception: Throwable) {
            log.error("Error occured when emitting the task entries", exception)
        }
    }

    override fun initialValue() = GetEntriesResult(
        remainingEntries = emptyList(),
        doneEntries = emptyList()
    )
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