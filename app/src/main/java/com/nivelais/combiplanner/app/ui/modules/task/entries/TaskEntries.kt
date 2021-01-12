package com.nivelais.combiplanner.app.ui.modules.task.entries

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nivelais.combiplanner.R
import com.nivelais.combiplanner.domain.entities.TaskEntry

fun LazyListScope.taskEntries(
    entries: List<TaskEntry>,
    onEntriesUpdated: (List<TaskEntry>) -> Unit,
    viewModel: TaskEntriesViewModel = TaskEntriesViewModel(entries = entries)
) {
    apply {
        item {
            Text(
                text = stringResource(id = R.string.task_entries_title),
                style = MaterialTheme.typography.body2
            )
        }

        // Items with the task entry
        itemsIndexed(viewModel.entriesState) { index, entry ->

            // A line with the entry name, done status and delete listener
            TaskEntryLine(
                name = entry.nameState.value,
                onNameChange = {
                    entry.nameState.value = it
                    onEntriesUpdated(viewModel.getUpdatedEntries())
                },
                isDone = entry.isDoneState.value,
                onDoneChange = {
                    entry.isDoneState.value = it
                    onEntriesUpdated(viewModel.getUpdatedEntries())
                },
                onDeleteClick = {
                    viewModel.deleteEntry(index = index)
                    onEntriesUpdated(viewModel.getUpdatedEntries())
                }
            )
        }

        // Item to create a new task
        item {
            Spacer(modifier = Modifier.padding(16.dp))
            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    viewModel.addEntry()
                    onEntriesUpdated(viewModel.getUpdatedEntries())
                }
            ) {
                AddEntryButtonContent()
            }
        }
    }
}

@Composable
private fun TaskEntryLine(
    name: String,
    onNameChange: (String) -> Unit,
    isDone: Boolean,
    onDoneChange: (Boolean) -> Unit,
    onDeleteClick: (() -> Unit),
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isDone,
            onCheckedChange = onDoneChange
        )
        Spacer(modifier = Modifier.padding(8.dp))
        TextField(
            modifier = Modifier.weight(1f),
            value = name,
            onValueChange = onNameChange,
            placeholder = {
                Text(text = stringResource(id = R.string.task_entries_name_placeholder))
            })
        // In the case of a new entry we display a little button to add the task
        IconButton(
            onClick = onDeleteClick
        ) {
            Icon(Icons.Default.Delete)
        }
    }
}

@Composable
private fun AddEntryButtonContent() {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.AddCircle)
        Spacer(modifier = Modifier.padding(8.dp))
        Text(text = stringResource(id = R.string.task_entries_add_button))
    }
}