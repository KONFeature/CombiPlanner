package com.nivelais.combiplanner.app.ui.modules.task.entries

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.More
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nivelais.combiplanner.R
import com.nivelais.combiplanner.domain.entities.TaskEntry
import androidx.core.app.ActivityCompat.startActivityForResult

import android.content.Intent
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.ImeAction
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf


@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun TaskEntries(
    taskId: Long?,
    viewModel: TaskEntriesViewModel = getViewModel { parametersOf(taskId) },
    header: LazyListScope.() -> Unit,
    footer: LazyListScope.() -> Unit
) {
    viewModel.entriesState.collectAsState()

    LazyColumn {
        header()


        // Remaining entries
        taskEntriesPart(
            titleRes = R.string.task_entries_todo_title,
            entries = viewModel.remainingEntriesState,
            viewModel = viewModel
        )

        // Done entries
        taskEntriesPart(
            titleRes = R.string.task_entries_done_title,
            entries = viewModel.doneEntriesState,
            viewModel = viewModel
        )


        footer()
    }
}

private fun LazyListScope.taskEntriesPart(
    @StringRes titleRes: Int,
    entries: SnapshotStateList<TaskEntry>,
    viewModel: TaskEntriesViewModel
) {
    // If we got no entry we can exit directly
    if (entries.isEmpty()) return

    item {
        Text(
            text = stringResource(id = titleRes),
            style = MaterialTheme.typography.body2
        )
    }

    // Items with the task entry
    items(items = entries) { entry ->
        val taskName = mutableStateOf(entry.name)
        val isDone = mutableStateOf(entry.isDone)

        // A line with the entry name, done status and delete listener
        TaskEntryLine(
            name = taskName.value,
            onNameChange = {
                taskName.value = it
            },
            isDone = isDone.value,
            onDoneChange = {
                isDone.value = it
            },
            onDeleteClick = {
                viewModel.deleteEntry(entryId = entry.id)
            }
        )
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isDone,
            onCheckedChange = onDoneChange
        )
        Spacer(modifier = Modifier.padding(8.dp))
        OutlinedTextField(
            modifier = Modifier.weight(1f),
            value = name,
            onValueChange = onNameChange,
            placeholder = {
                Text(text = stringResource(id = R.string.task_entries_name_placeholder))
            },
            onImeActionPerformed = { action, _ ->
                if (action == ImeAction.Done) {
                    // TODO : Perform an update of the entity
                }
            }
        )
        // In the case of a new entry we display a little button to add the task
        IconButton(
            onClick = onDeleteClick
        ) {
            Icon(Icons.Default.Delete, "Delete the entry")
        }
    }
}

@Composable
fun AddEntryLine(
    onAddClick: () -> Unit
) {
    Row {
        OutlinedButton(
            modifier = Modifier.weight(1f),
            onClick = {
                onAddClick()
            }
        ) {
            AddEntryButtonContent()
        }
        IconButton(
            onClick = {
                // TODO : Show / Hide advanced entry menu (card -> upload pics + depends on other task + description)
//                val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                // TODO : How to launch the intent within jetpack compose ??
            }
        ) {
            Icon(Icons.Filled.ExpandMore, "Show advanced entry options")
        }
    }
}

@Composable
private fun AddEntryButtonContent() {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.AddCircle, "Add an entry")
        Spacer(modifier = Modifier.padding(8.dp))
        Text(text = stringResource(id = R.string.task_entries_add_button))
    }
}