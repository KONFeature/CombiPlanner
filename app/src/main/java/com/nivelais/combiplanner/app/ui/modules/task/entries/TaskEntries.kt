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
import com.nivelais.combiplanner.app.di.getViewModel
import com.nivelais.combiplanner.app.ui.modules.task.entry.TaskEntryView
import com.nivelais.combiplanner.app.utils.safeItems
import com.nivelais.combiplanner.domain.entities.TaskEntry
import kotlinx.coroutines.ExperimentalCoroutinesApi


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

    LazyColumn {
        header()

        // Entries
        taskEntriesPart(
            titleRes = R.string.task_entries_title,
            entries = viewModel.entries
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
            style = MaterialTheme.typography.subtitle1
        )
    }

    // Items with the task entry
    safeItems(entries) {
        TaskEntryView(taskEntry = it)
    }
}