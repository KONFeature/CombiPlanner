package com.nivelais.combiplanner.app.ui.modules.task.add_entry

import androidx.activity.compose.registerForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Photo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    Column {
        var isAdvancedOptionsVisible by remember { viewModel.isAdvancedOptionsVisibleState }

        // The launcher used to get a picture
        val pictureResultLauncher = registerForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult(),
            onResult = { viewModel.handlePictureResult(it) })

        AddEntryButton(
            isAdvancedOptionsVisible = isAdvancedOptionsVisible,
            isEnable = isEnable,
            onAddClick = {
                taskId?.let { viewModel.addEntry(taskId = it) }
            },
            onToggleAdvancedOptionClick = {
                isAdvancedOptionsVisible = !isAdvancedOptionsVisible
            }
        )

        if (isAdvancedOptionsVisible) {
            AdvancedEntryCard {
                Text("Discover more options here soon !")
                IconButton(onClick = {
                    pictureResultLauncher.launch(viewModel.intentForPicture)
                }) {
                    Icon(Icons.Filled.Photo, "Take a picture")
                }
            }
        }
    }
}

@Composable
private fun AddEntryButton(
    isAdvancedOptionsVisible: Boolean,
    isEnable: Boolean,
    onAddClick: () -> Unit,
    onToggleAdvancedOptionClick: () -> Unit,
) {

    Row {
        OutlinedButton(
            modifier = Modifier.weight(1f),
            enabled = isEnable,
            onClick = onAddClick
        ) {
            AddEntryButtonContent()
        }
        IconButton(
            enabled = isEnable,
            onClick = onToggleAdvancedOptionClick
        ) {
            val icon =
                if (isAdvancedOptionsVisible) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore
            Icon(icon, "Toggle the advanced entry creation options")
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


@Composable
private fun AdvancedEntryCard(
    content: @Composable ColumnScope.() -> Unit
) {
    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            content = content
        )
    }
}