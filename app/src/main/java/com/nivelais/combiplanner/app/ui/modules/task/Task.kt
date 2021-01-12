package com.nivelais.combiplanner.app.ui.modules.task

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nivelais.combiplanner.R
import com.nivelais.combiplanner.app.ui.modules.task.entries.TaskEntries
import com.nivelais.combiplanner.app.ui.widgets.CategoryPicker
import com.nivelais.combiplanner.domain.usecases.task.DeleteTaskResult
import com.nivelais.combiplanner.domain.usecases.task.SaveTaskResult
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
    onActive {
        // Load the base task
        viewModel.getInitialTask(taskId)
    }

    // Check the loading state (if we are loading the view exit with a progress indicator)
    val isLoading by remember { viewModel.isLoadingState }
    if (isLoading) {
        // TODO : Progress indicator
        return
    }

    // The state of the save process
    val saveState = viewModel.saveFlow.collectAsState()
    if (saveState.value == SaveTaskResult.SUCCESS) {
        // If the save is in success we go back
        navController.popBackStack()
    }

    // The state of the delete process
    val deleteState = viewModel.deleteFlow.collectAsState()
    if (deleteState.value == DeleteTaskResult.SUCCESS) {
        // If the delete is in success we go back
        navController.popBackStack()
    }

    val errorRes by remember { viewModel.errorResState }
    val isNameError by remember { viewModel.isNameInErrorState }

    // The state for the name and category of this task
    var name by remember { viewModel.nameState }
    var category by remember { viewModel.categoryState }

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
                isError = isNameError,
                onNameChange = { name = it }
            )
            Spacer(modifier = Modifier.padding(8.dp))
            // Save button
            IconButton(onClick = { viewModel.save() }) {
                Icon(Icons.Default.Save)
            }
            // Delete button if that's not a new task
            if (viewModel.initialTaskId != null) {
                Spacer(modifier = Modifier.padding(8.dp))
                IconButton(onClick = { viewModel.delete() }) {
                    Icon(Icons.Default.Delete)
                }
            }
        }
        Spacer(modifier = Modifier.padding(8.dp))

        // If we got an error display it
        errorRes?.let {
            Text(
                text = stringResource(id = it),
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.error
            )
        }

        LazyColumn {

            // Category selection
            item {
                Text(
                    text = stringResource(id = R.string.task_category_title),
                    style = MaterialTheme.typography.body2
                )
                Spacer(modifier = Modifier.padding(8.dp))
                viewModel.categoriesFlow
                    .collectAsState()
                    .value?.let { categories ->
                        CategoryPicker(
                            categories = categories,
                            categorySelected = category,
                            onCategoryPicked = {
                                category = it
                            })
                    } ?: run {
                    Text(text = stringResource(id = R.string.task_category_loading))
                }
                Spacer(modifier = Modifier.padding(8.dp))
            }

            // Task entries
            // The entries for our task
            TaskEntries(
                scope = this,
                entries = viewModel.entries,
                onEntriesUpdated = {
                    viewModel.entries = it
                })
        }
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