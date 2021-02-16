package com.nivelais.combiplanner.app.ui.modules.task.add_entry

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nivelais.combiplanner.R
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.compose.getViewModel


@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun AddEntryLine(
    taskId: Long?,
    viewModel: AddEntryViewModel = getViewModel(),
) {
    Row {
        OutlinedButton(
            modifier = Modifier.weight(1f),
            onClick = {
                taskId?.let { viewModel.addEntry(taskId = it) }
            }
        ) {
            AddEntryButtonContent()
        }
        IconButton(
            onClick = {
                // TODO : Develop advanced option
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