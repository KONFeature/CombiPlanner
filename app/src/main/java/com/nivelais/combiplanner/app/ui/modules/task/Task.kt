package com.nivelais.combiplanner.app.ui.modules.task

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nivelais.combiplanner.R
import com.nivelais.combiplanner.app.ui.modules.category.picker.CategoryPicker
import com.nivelais.combiplanner.app.ui.modules.task.add_entry.AddEntryLine
import com.nivelais.combiplanner.app.ui.modules.task.entries.TaskEntries
import com.nivelais.combiplanner.domain.usecases.task.DeleteTaskResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.compose.get
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun TaskPage(
    viewModel: TaskViewModel = getViewModel(),
    navController: NavController = get(),
    taskId: Long?,
) {
    DisposableEffect(taskId) {
        // Load the initial task
        viewModel.loadInitialTask(taskId)

        onDispose {
            // If the user exit directly dispose this job
            viewModel.disposeLoadInitialTask()
        }
    }

    // Check the loading state (if we are loading the view exit with a progress indicator)
    val isLoading by remember { viewModel.isLoadingState }
    if (isLoading) {
        // TODO : Progress indicator
        return
    }

    // The state of the delete process
    val deleteState = viewModel.deleteFlow.collectAsState()
    if (deleteState.value == DeleteTaskResult.SUCCESS) {
        // If the delete is in success we go back
        navController.popBackStack()
    }

    // The state of the ressource for the error occured (if any)
    val errorTextRessource by remember { viewModel.errorResState }

    // The state for the name and category of this task
    var name by remember { viewModel.nameState }

    // The state for our current task id
    val currentTaskId by remember { viewModel.idState }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header part
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Name of the task
            NameInput(
                modifier = Modifier.weight(1f),
                name = name,
                isError = errorTextRessource != null,
                onNameChange = {
                    name = it
                    viewModel.save()
                })
            // Delete button if that's not a new task
            if (currentTaskId != null) {
                Spacer(modifier = Modifier.padding(8.dp))
                IconButton(onClick = { viewModel.delete() }) {
                    Icon(Icons.Default.Delete, "Delete this task")
                }
            }
        }
        Spacer(modifier = Modifier.padding(8.dp))

        // If we got an error display it
        errorTextRessource?.let {
            Text(
                text = stringResource(id = it),
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.error
            )
        }

        // The entries for our task (only if we got a task in the database)
        TaskEntries(
            taskId = currentTaskId,
            header = {
                // Category selection
                item {
                    Text(
                        text = stringResource(id = R.string.task_category_title),
                        style = MaterialTheme.typography.body2
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                    CategoryPicker(
                        initialCategory = viewModel.categoryState.value,
                        onCategoryPicked = {
                            viewModel.categoryState.value = it
                            viewModel.save()
                        })
                    Spacer(modifier = Modifier.padding(8.dp))
                }
            },
            footer = {
                // Item to create a new task
                item {
                    Spacer(modifier = Modifier.padding(16.dp))
                    AddEntryLine(taskId = currentTaskId)
                }
            }
        )
    }
}

@Composable
private fun NameInput(
    name: String,
    onNameChange: (String) -> Unit,
    isError: Boolean,
    modifier: Modifier
) {
    OutlinedTextField(
        value = name,
        onValueChange = onNameChange,
        singleLine = true,
        modifier = modifier,
        isErrorValue = isError,
        label = {
            Text(text = stringResource(id = R.string.task_name_label))
        }
    )
}