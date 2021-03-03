package com.nivelais.combiplanner.app.ui.modules.task.add_entry

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nivelais.combiplanner.R
import com.nivelais.combiplanner.app.di.getViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi


@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun AddEntry(
    taskId: Long?,
    isEnable: Boolean = taskId != null,
    viewModel: AddEntryViewModel = getViewModel(),
) {
    Row {
        OutlinedButton(
            modifier = Modifier.weight(1f),
            enabled = isEnable,
            onClick = {
                taskId?.let { viewModel.addEntry(taskId = it) }
            }
        ) {
            AddEntryButtonContent()
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