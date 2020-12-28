package com.nivelais.combiplanner.app.ui.modules.task

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.nivelais.combiplanner.app.ui.modules.main.GenericViewModel
import com.nivelais.combiplanner.domain.entities.Category
import com.nivelais.combiplanner.domain.entities.Task
import com.nivelais.combiplanner.domain.entities.TaskEntry
import com.nivelais.combiplanner.domain.usecases.*
import com.nivelais.combiplanner.domain.usecases.category.GetCategoriesUseCase
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

    /**
     * State flow of our categories
     */
    val categoriesFlow = getCategoriesUseCase.stateFlow

    /**
     * The flow pour the save of this task
     */
    val saveFlow = saveTaskUseCase.stateFlow

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
     * Init the base field for our view when the task is loaded
     */
    private fun initBaseField(task: Task?) {
        nameState = mutableStateOf(task?.name ?: "")
        categoryState = mutableStateOf(task?.category)
        entries = task?.entries ?: emptyList()
    }

    /**
     * Call the use case to fetch all of our category
     */
    fun fetchCategories() = getCategoriesUseCase.run(Unit)

    /**
     * Save this task
     */
    fun save() {
        // TODO : Better way to do this
        // TODO : Error text when no category picked / name input is null
        categoryState.value?.let { category ->
            val params = SaveTaskParams(
                id = initialTaskId,
                category = category,
                name = nameState.value,
                description = null,
                entries = entries
            )
            saveTaskUseCase.run(params)
        }
    }

    /**
     * Delete this task
     */
    fun delete() {
        // TODO : Do it
    }

    override fun clearUseCases() {
        getTaskUseCase.clear()
        getCategoriesUseCase.clear()
        saveTaskUseCase.clear()
    }
}