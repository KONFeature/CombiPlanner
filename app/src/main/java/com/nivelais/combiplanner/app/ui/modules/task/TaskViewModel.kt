package com.nivelais.combiplanner.app.ui.modules.task

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.nivelais.combiplanner.R
import com.nivelais.combiplanner.app.ui.modules.main.GenericViewModel
import com.nivelais.combiplanner.domain.entities.Category
import com.nivelais.combiplanner.domain.entities.Task
import com.nivelais.combiplanner.domain.entities.TaskEntry
import com.nivelais.combiplanner.domain.usecases.category.GetCategoriesUseCase
import com.nivelais.combiplanner.domain.usecases.task.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.core.scope.inject

class TaskViewModel : GenericViewModel() {

    /**
     * The initial task id used to init this view
     */
    var initialTaskId: Long? = null

    /**
     * Value to know if we are currently loading the initial task
     */
    val isLoadingState = mutableStateOf(true)

    /*
     * The states for the name, category and entries of the current task
     */
    lateinit var nameState: MutableState<String>
    lateinit var categoryState: MutableState<Category?>
    lateinit var entries: List<TaskEntry>

    // Use case to get a task by it's id
    private val getTaskUseCase: GetTaskUseCase by inject()

    // Use case to list all the categories
    private val getCategoriesUseCase: GetCategoriesUseCase by inject()

    // Use case to save our task
    private val saveTaskUseCase: SaveTaskUseCase by inject()

    // Use case to delete our task
    private val deleteTaskUseCase: DeleteTaskUseCase by inject()

    // If we got any error put it here
    val errorResState: MutableState<Int?> = mutableStateOf(null)

    // If we an error on the name part
    val isNameInErrorState: MutableState<Boolean> = mutableStateOf(false)

    /**
     * State flow of our categories
     */
    val categoriesFlow = getCategoriesUseCase.stateFlow

    /**
     * The flow for the saving of this task
     */
    val saveFlow = saveTaskUseCase.stateFlow

    /**
     * The flow pour the deletion of this task
     */
    val deleteFlow = deleteTaskUseCase.stateFlow

    init {
        viewModelScope.launch {
            // Load all the categories
            getCategoriesUseCase.execute(Unit)
            // Listen to the save status
            saveTaskUseCase.stateFlow.collect {
                when (it) {
                    SaveTaskResult.WAITING -> {
                        // Nothing ??
                    }
                    SaveTaskResult.SUCCESS -> {
                        // Tell the view to go back ?
                        errorResState.value = null
                        isNameInErrorState.value = false
                    }
                    SaveTaskResult.INVALID_NAME -> {
                        errorResState.value = R.string.task_save_error_invalid_name
                        isNameInErrorState.value = true
                    }
                    SaveTaskResult.DUPLICATE_NAME -> {
                        errorResState.value = R.string.task_save_error_duplicate_name
                        isNameInErrorState.value = true
                    }
                    SaveTaskResult.ERROR -> {
                        errorResState.value = R.string.task_save_error
                        isNameInErrorState.value = false
                    }
                }
            }
        }
    }

    /**
     * Get the initial task
     */
    fun getInitialTask(id: Long?) {
        log.info("Loading task for the id '$id'")
        // Convert not found id to null
        val usableId = if (id == -1L || id == 0L) null else id
        // Backup the initial task id
        initialTaskId = usableId
        // Observe the get task flow
        viewModelScope.launch {
            getTaskUseCase.stateFlow.collect { taskResult ->
                when (taskResult) {
                    is GetTaskResult.Loading -> {
                        isLoadingState.value = true
                    }
                    is GetTaskResult.NotFound,
                    is GetTaskResult.Success -> {
                        initBaseField(taskResult.task)
                        isLoadingState.value = false
                    }
                }
            }
        }
        // Once the observer is in place run it
        getTaskUseCase.run(GetTaskParams(id = usableId))
    }

    /**
     * Dispose the get task job
     */
    fun disposeGetTaskJob() {
        getTaskUseCase.clear()
    }

    /**
     * Init the base field for our view when the task is loaded
     */
    private fun initBaseField(task: Task?) {
        nameState = mutableStateOf(task?.name ?: "")
        categoryState = mutableStateOf(task?.category)
        entries = task?.entries ?: emptyList()
    }

    /**
     * Save this task
     */
    fun save() {
        // If a category is picked launch the save job
        categoryState.value?.let { category ->
            val params = SaveTaskParams(
                id = initialTaskId,
                category = category,
                name = nameState.value,
                entries = entries
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
        initialTaskId?.let { taskId ->
            deleteTaskUseCase.run(DeleteTaskParams(id = taskId))
        }
    }

    override fun clearUseCases() {
        getTaskUseCase.clear()
        getCategoriesUseCase.clear()
        saveTaskUseCase.clear()
    }
}