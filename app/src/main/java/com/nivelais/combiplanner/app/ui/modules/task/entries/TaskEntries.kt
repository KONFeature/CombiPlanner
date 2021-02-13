package com.nivelais.combiplanner.app.ui.modules.task.entries

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nivelais.combiplanner.R
import com.nivelais.combiplanner.app.ui.modules.task.entry.TaskEntryView
import com.nivelais.combiplanner.domain.entities.TaskEntry
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
    val entries = viewModel.entriesState.collectAsState()

    LazyColumn {
        header()

        // Remaining entries
        taskEntriesPart(
            titleRes = R.string.task_entries_todo_title,
            entries = entries.value.remainingEntries
        )

        // Done entries
        taskEntriesPart(
            titleRes = R.string.task_entries_done_title,
            entries = entries.value.doneEntries
        )

        footer()
    }
}

private fun LazyListScope.taskEntriesPart(
    @StringRes titleRes: Int,
    entries: List<TaskEntry>,
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
        // A line with the entry name, done status and delete listener
        TaskEntryView(taskEntry = entry)
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