/*
 * Copyright 2020-2021 Quentin Nivelais
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nivelais.combiplanner.app.ui.modules.task

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nivelais.combiplanner.R
import org.koin.androidx.compose.get
import org.koin.androidx.compose.getViewModel
import com.nivelais.combiplanner.app.ui.modules.category.picker.CategoryPicker
import com.nivelais.combiplanner.app.ui.modules.task.add_entry.AddEntry
import com.nivelais.combiplanner.app.ui.modules.task.entries.TaskEntries
import kotlinx.coroutines.ExperimentalCoroutinesApi

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

    // Check the go back state
    val needToGoBack by remember { viewModel.isNeedGoBackState }
    if (needToGoBack) {
        // If the delete is in success we go back
        navController.popBackStack()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // The state for the error text, task name, id, and is category displayed
        var name by remember { viewModel.nameState }
        val errorTextResource by remember { viewModel.errorResState }
        val currentTaskId by remember { viewModel.idState }
        var isCategoryPickerDisplayed by remember { viewModel.isCategoryPickerDisplayedState }

        // Header part
        Header(
            name = name,
            onNameChanged = {
                name = it
                viewModel.save()
            },
            onBackClick = { navController.popBackStack() },
            isDeleteEnabled = currentTaskId != null,
            onDeleteClick = { viewModel.delete() },
            errorTextResource = errorTextResource,
        )

        // Category selection
        CategoryPickerHeader(
            isDroppedDown = isCategoryPickerDisplayed,
            onDropDownClicked = {
                isCategoryPickerDisplayed = !isCategoryPickerDisplayed
            }
        )
        if (isCategoryPickerDisplayed) {
            Spacer(modifier = Modifier.padding(8.dp))
            CategoryPicker(
                initialCategory = viewModel.categoryState.value,
                onCategoryPicked = {
                    viewModel.categoryState.value = it
                    viewModel.save()
                }
            )
        }

        // The entries for our task (only if we got a task in the database)
        Divider(thickness = 1.dp, modifier = Modifier.padding(8.dp))
        TaskEntries(
            taskId = currentTaskId,
            footer = {
                // Item to create a new task
                item {
                    Spacer(modifier = Modifier.padding(8.dp))
                    AddEntry(taskId = currentTaskId)
                }
            }
        )
    }
}

@Composable
private fun Header(
    name: String,
    onNameChanged: (String) -> Unit,
    onBackClick: () -> Unit,
    isDeleteEnabled: Boolean,
    onDeleteClick: () -> Unit,
    errorTextResource: Int?
) {
    // The state for the error text, task name, id, and is category displayed
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Button to go back
        IconButton(onClick = onBackClick) {
            Icon(Icons.Filled.ArrowBack, "Go back to the previous activity")
        }
        Spacer(modifier = Modifier.padding(8.dp))
        // Name of the task
        NameInput(
            modifier = Modifier.weight(1f),
            name = name,
            isError = errorTextResource != null,
            onNameChange = onNameChanged
        )
        // Delete button if that's not a new task
        Spacer(modifier = Modifier.padding(8.dp))
        IconButton(
            onClick = onDeleteClick,
            enabled = isDeleteEnabled
        ) {
            Icon(Icons.Default.Delete, "Delete this task")
        }
    }
    Spacer(modifier = Modifier.padding(8.dp))

    // If we got an error display it
    errorTextResource?.let {
        Text(
            text = stringResource(id = it),
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.error
        )
        Spacer(modifier = Modifier.padding(8.dp))
    }
}

@Composable
private fun CategoryPickerHeader(
    isDroppedDown: Boolean,
    onDropDownClicked: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = R.string.task_category_title),
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = onDropDownClicked) {
            val icon = if (isDroppedDown) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore
            Icon(icon, "Toggle the category picker visibility")
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
        isError = isError,
        label = {
            Text(text = stringResource(id = R.string.task_name_label))
        }
    )
}
