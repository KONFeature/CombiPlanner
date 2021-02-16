package com.nivelais.combiplanner.app.ui.modules.task

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.nivelais.combiplanner.R
import com.nivelais.combiplanner.app.ui.modules.main.GenericViewModel
import com.nivelais.combiplanner.domain.entities.Category
import com.nivelais.combiplanner.domain.entities.Task
import com.nivelais.combiplanner.domain.usecases.task.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.core.scope.inject

class TaskViewModel : GenericViewModel() {

    // Use case to get a task by it's id
    private val getTaskUseCase: GetTaskUseCase by inject()

    // Use case to save our task
    private val saveTaskUseCase: SaveTaskUseCase by inject()

    // Use case to delete our task
    private val deleteTaskUseCase: DeleteTaskUseCase by inject()

    /**
     * Value to know if we are currently loading the initial task
     */
    val isLoadingState = mutableStateOf(true)

    /**
     * The state for the id of the current task
     */
    var idState: MutableState<Long?> = mutableStateOf(null)
        private set

    /*
     * The states for the name, category and entries of the current task
     */
    lateinit var nameState: MutableState<String>
    lateinit var categoryState: MutableState<Category?>

    // If we got any error put it here
    val errorResState: MutableState<Int?> = mutableStateOf(null)

    /**
     * The flow pour the deletion of this task
     */
    val deleteFlow : StateFlow<DeleteTaskResult>
        get() = deleteTaskUseCase.stateFlow

    // The listener on the saving use case
    private var loadInitialTaskJob: Job? = null

    init {
        viewModelScope.launch {
            // Listen to the save status
            saveTaskUseCase.stateFlow.collectLatest {
                when (it) {
                    SaveTaskResult.Waiting -> {
                        // TODO : Small progress indicator next to the task title
                    }
                    is SaveTaskResult.Success -> {
                        // Remove all the error and update the current id
                        errorResState.value = null
                        idState.value = it.taskId
                    }
                    SaveTaskResult.InvalidName -> {
                        errorResState.value = R.string.task_save_error_invalid_name
                    }
                    SaveTaskResult.DuplicateName -> {
                        errorResState.value = R.string.task_save_error_duplicate_name
                    }
                    SaveTaskResult.Error -> {
                        errorResState.value = R.string.task_save_error
                    }
                }
            }
        }
    }

    /**
     * Get the initial task
     */
    fun loadInitialTask(id: Long?) {
        log.info("Loading task for the id '$id'")
        // Convert not found id to null
        val usableId = if (id == -1L || id == 0L) null else id
        // Backup the initial task id
        idState.value = usableId
        // Observe the get task flow
        loadInitialTaskJob = viewModelScope.launch {
            getTaskUseCase.stateFlow.collect { taskResult ->
                when (taskResult) {
                    GetTaskResult.Loading -> {
                        isLoadingState.value = true
                    }
                    GetTaskResult.NotFound,
                    is GetTaskResult.Success -> {
                        initBaseField(taskResult.task)
                        isLoadingState.value = false
                    }
                }
            }
        }
        // Once the observer is in place run it
        getTaskUseCase.run(GetTaskParams(id = idState.value))
    }

    /**
     * Init the base field for our view when the task is loaded
     */
    private fun initBaseField(task: Task?) {
        nameState = mutableStateOf(task?.name ?: "")
        categoryState = mutableStateOf(task?.category)
        idState.value = task?.id
    }

    /**
     * Dispose the get task job
     */
    fun disposeLoadInitialTask() {
        loadInitialTaskJob?.cancel()
    }

    /**
     * Save this task
     */
    fun save() {
        // If a category is picked launch the save job
        categoryState.value?.let { category ->
            val params = SaveTaskParams(
                id = idState.value,
                category = category,
                name = nameState.value
            )
            saveTaskUseCase.run(params)
        } ?: run {
            // Else display an error
            errorResState.value = R.string.task_save_error_no_category
        }
    }

    /**
     * Delete this task
     */
    fun delete() {
        idState.value?.let { taskId ->
            deleteTaskUseCase.run(DeleteTaskParams(id = taskId))
        }
    }

    override fun clearUseCases() {
        getTaskUseCase.clear()
        saveTaskUseCase.clear()
        deleteTaskUseCase.clear()
    }
}