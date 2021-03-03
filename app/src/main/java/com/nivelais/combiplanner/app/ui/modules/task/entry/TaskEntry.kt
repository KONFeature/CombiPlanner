package com.nivelais.combiplanner.app.ui.modules.task.entry

import androidx.activity.compose.registerForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Photo
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nivelais.combiplanner.R
import com.nivelais.combiplanner.app.di.get
import com.nivelais.combiplanner.app.utils.safeItems
import com.nivelais.combiplanner.domain.entities.TaskEntry
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun TaskEntryView(
    taskEntry: TaskEntry,
    viewModel: TaskEntryViewModel = get(),
) {
    // Update the entry only when the id change
    DisposableEffect(taskEntry.id) {
        viewModel.updateEntry(taskEntry)

        onDispose { } // TODO : How to force reload the current entry ???
    }

    // Element of the entry itself
    val name by remember { viewModel.nameState }
    val isDone by remember { viewModel.isDoneState }

    // Is the advanced panne lvisible
    var isAdvancedVisible by remember { viewModel.isAdvancedVisibleState }

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
        // TODO : Replace delete button with arrow that toggle the detail of the entry and the advanced option (adding picture, dependency on other task etc)
        // TODO : No card for the advanced pannel, maybe a divider at the bottom ?
        // TODO : Or advanced pannel = alert ? With the
        IconButton(
            onClick = { isAdvancedVisible = !isAdvancedVisible }
        ) {
            val icon = if (isAdvancedVisible) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore
            Icon(icon, "Toggle the advanced entry pannel")
        }
        IconButton(
            onClick = { viewModel.onDeleteClicked() }
        ) {
            Icon(Icons.Default.Delete, "Delete the entry")
        }
    }

    if (isAdvancedVisible) {
        AdvancedEntryCard {
            // The launcher used to get a picture
            val pictureResultLauncher = registerForActivityResult(
                contract = ActivityResultContracts.StartActivityForResult(),
                onResult = { viewModel.handlePictureResult(it) })

            Text("Discover more options here soon !")

            Spacer(modifier = Modifier.padding(8.dp))

            OutlinedButton(onClick = {
                pictureResultLauncher.launch(viewModel.intentForPicture)
            }) {
                Text(text = stringResource(id = R.string.task_entries_add_photo))
                Icon(Icons.Filled.Photo, "Take a picture")
            }

            // If we already got some pictures display them
            if (viewModel.pictures.isNotEmpty()) {
                Spacer(modifier = Modifier.padding(8.dp))
                Text("Current pictures that will be join with the entry")
                LazyRow {
                    safeItems(viewModel.pictures) { picture ->
                        Image(bitmap = picture.asImageBitmap(), contentDescription = "User picture")
                        Spacer(modifier = Modifier.padding(8.dp))
                    }
                }
            }
        }
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