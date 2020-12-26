package com.nivelais.combiplanner.app.ui.modules.task

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.nivelais.combiplanner.domain.usecases.SaveTaskResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun TaskPage(
    navController: NavController,
    taskId: Long?,
    viewModel: TaskViewModel = getViewModel(),
) {
    onActive {
        viewModel.fetchCategories()
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
    // TODO : Auto update ???
    // TODO : Error handling ??

    // The state for the name and category of this task
    var name by remember { viewModel.nameState }
    var category by remember { viewModel.categoryState }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        // Header part
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back button
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack)
            }
            // Name of the task
            NameInput(
                modifier = Modifier.weight(1f),
                name = name,
                onNameChange = { name = it }
            )
            Spacer(modifier = Modifier.padding(8.dp))
            // Save button
            IconButton(onClick = { viewModel.save() }) {
                Icon(Icons.Default.Save)
            }
        }

        // Category selection
        Spacer(modifier = Modifier.padding(8.dp))
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

        // The entries for our task
        TaskEntries(
            entries = viewModel.entries,
            onEntriesUpdated = {
                viewModel.entries = it
            })
    }
}

@Composable
private fun NameInput(
    name: String,
    onNameChange: (String) -> Unit,
    modifier: Modifier
) {
    OutlinedTextField(
        value = name,
        onValueChange = onNameChange,
        singleLine = true,
        modifier = modifier,
        label = {
            Text(text = stringResource(id = R.string.task_name_label))
        }
    )
}