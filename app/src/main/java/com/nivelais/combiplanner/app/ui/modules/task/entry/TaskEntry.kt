package com.nivelais.combiplanner.app.ui.modules.task.entry

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nivelais.combiplanner.R
import com.nivelais.combiplanner.domain.entities.TaskEntry
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.compose.get
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun TaskEntryView(
    taskEntry: TaskEntry,
    viewModel: TaskEntryViewModel = get { parametersOf(taskEntry) },
) {
    val name by remember { viewModel.nameState }
    val isDone by remember { viewModel.isDoneState }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isDone,
            onCheckedChange = { viewModel.onIsDoneChanged(it) },
        )
        Spacer(modifier = Modifier.padding(8.dp))
        OutlinedTextField(
            modifier = Modifier.weight(1f),
            value = name,
            onValueChange = { viewModel.onNameChange(it) },
            placeholder = {
                Text(text = stringResource(id = R.string.task_entries_name_placeholder))
            },
        )
        // In the case of a new entry we display a little button to add the task
        IconButton(
            onClick = { viewModel.onDeleteClicked() }
        ) {
            Icon(Icons.Default.Delete, "Delete the entry")
        }
    }
}