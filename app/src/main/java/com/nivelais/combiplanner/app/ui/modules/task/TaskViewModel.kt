package com.nivelais.combiplanner.app.ui.modules.task

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.nivelais.combiplanner.R
import com.nivelais.combiplanner.app.ui.modules.main.GenericViewModel
import com.nivelais.combiplanner.app.utils.runAndCollect
import com.nivelais.combiplanner.domain.entities.Category
import com.nivelais.combiplanner.domain.entities.Task
import com.nivelais.combiplanner.domain.usecases.task.DeleteTaskParams
import com.nivelais.combiplanner.domain.usecases.task.DeleteTaskResult
import com.nivelais.combiplanner.domain.usecases.task.DeleteTaskUseCase
import com.nivelais.combiplanner.domain.usecases.task.GetTaskParams
import com.nivelais.combiplanner.domain.usecases.task.GetTaskResult
import com.nivelais.combiplanner.domain.usecases.task.GetTaskUseCase
import com.nivelais.combiplanner.domain.usecases.task.SaveTaskParams
import com.nivelais.combiplanner.domain.usecases.task.SaveTaskResult
import com.nivelais.combiplanner.domain.usecases.task.SaveTaskUseCase
import kotlinx.coroutines.Job
import org.koin.core.scope.inject

class TaskViewModel : GenericViewModel() {

    // Use case to get a task by it's id
    private val getTaskUseCase: GetTaskUseCase by inject()

    // Use case to save our task
    private val saveTaskUseCase: SaveTaskUseCase by inject()

    // Use case to delete our task
    private val deleteTaskUseCase: DeleteTaskUseCase by inject()

    // State used to know if we are currently loading the initial task
    val isLoadingState = mutableStateOf(true)

    // State to know if we need to go back or not
    val isNeedGoBackState = mutableStateOf(false)

    // The state for the id of the current task
    var idState: MutableState<Long?> = mutableStateOf(null)

    /**
     * State used to know if the category picker is displayed or not
     */
    val isCategoryPickerDisplayedState = mutableStateOf(true)

    /*
     * The states for the name, category and entries of the current task
     */
    lateinit var nameState: MutableState<String>
    lateinit var categoryState: MutableState<Category?>

    // If we got any error put it here
    val errorResState: MutableState<Int?> = mutableStateOf(null)

    // The listener on the loading use case
    private var loadInitialTaskJob: Job? = null

    // The listener on the saving use case
    private var saveListenerJob: Job? = null

    // The listener on the delete use case
    private var deleteListenerJob: Job? = null

    /**
     * Get the initial task
     */
    fun loadInitialTask(id: Long?) {
        log.info("Loading task for the id '$id'")
        // Convert not found id to null
        val usableId = if (id == -1L || id == 0L) null else id
        // Hide the category picker if the value isn't null
        isCategoryPickerDisplayedState.value = usableId == null
        // Backup the initial task id
        idState.value = usableId
        // Observe the get task flow
        loadInitialTaskJob?.cancel()
        loadInitialTaskJob = getTaskUseCase.runAndCollect(
            GetTaskParams(id = idState.value),
            viewModelScope
        ) { taskResult ->
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
            saveTask(category = category)
        } ?: run {
            // Else display an error
            errorResState.value = R.string.task_save_error_no_category
        }
    }

    /**
     * Launch the save task use case
     */
    private fun saveTask(category: Category) {
        saveListenerJob?.cancel()
        saveListenerJob = saveTaskUseCase.runAndCollect(
            SaveTaskParams(
                id = idState.value,
                category = category,
                name = nameState.value
            ),
            viewModelScope
        ) { saveResult ->
            when (saveResult) {
                SaveTaskResult.Loading -> {
                    // TODO : Small progress indicator next to the task title
                }
                is SaveTaskResult.Success -> {
                    // Remove all the error and update the current id
                    errorResState.value = null
                    idState.value = saveResult.taskId
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

    /**
     * Delete this task
     */
    fun delete() {
        idState.value?.let { taskId ->
            // No need to save the job, because when it's a success we clear the use case
            deleteListenerJob?.cancel()
            deleteListenerJob = deleteTaskUseCase.runAndCollect(
                DeleteTaskParams(id = taskId),
                viewModelScope
            ) {
                // If the deletion is in success tell the view to go back
                if (it == DeleteTaskResult.SUCCESS) {
                    isNeedGoBackState.value = true
                }
            }
        }
    }

    override fun clearUseCases() {
        loadInitialTaskJob?.cancel()
        saveListenerJob?.cancel()
        deleteListenerJob?.cancel()

        getTaskUseCase.clear()
        saveTaskUseCase.clear()
        deleteTaskUseCase.clear()
    }
}