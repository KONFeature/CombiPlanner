package com.nivelais.combiplanner.app.ui.modules.task.entries

import androidx.annotation.StringRes
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.res.stringResource
import com.nivelais.combiplanner.R
import com.nivelais.combiplanner.app.ui.modules.task.entry.TaskEntryView
import com.nivelais.combiplanner.domain.entities.TaskEntry
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.compose.getViewModel


@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun TaskEntries(
    taskId: Long?,
    viewModel: TaskEntriesViewModel = getViewModel(),
    header: LazyListScope.() -> Unit,
    footer: LazyListScope.() -> Unit
) {
    DisposableEffect(taskId) {
        // Start listening for the task entries
        viewModel.listenToEntries(taskId = taskId)

        onDispose {
            // Close the listener
            viewModel.disposeEntriesListener()
        }
    }
//    val entries = viewModel.entriesState.collectAsState()
//    val entries = viewModel.entriesState.collectAsState()

    LazyColumn {
        header()

        // Remaining entries
        taskEntriesPart(
            titleRes = R.string.task_entries_todo_title,
            entries = viewModel.remainingEntries
        )

        // Done entries
        taskEntriesPart(
            titleRes = R.string.task_entries_done_title,
            entries = viewModel.doneEntries
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
    items(count = entries.size) { index ->
        // A line with the entry name, done status and delete listener
        entries.getOrNull(index)?.let { entry ->
            TaskEntryView(taskEntry = entry)
        }
    }
}