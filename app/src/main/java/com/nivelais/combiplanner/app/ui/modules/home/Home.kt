package com.nivelais.combiplanner.app.ui.modules.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.onActive
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nivelais.combiplanner.domain.entities.Task
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun Home(
    homeViewModel: HomeViewModel = getViewModel()
) {
    onActive {
        // Launch the tasks fetching
        homeViewModel.fetchTasks()
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp).fillMaxWidth()
    ) {
        Text(
            text = "Home",
            style = MaterialTheme.typography.h1
        )
        Tasks(taskState = homeViewModel.tasksFlow.collectAsState())

        // TODO : Create a new task
        // TODO : Manage category in the settings screen first
        FloatingActionButton(
            onClick = { /*TODO*/ }
        ) {
            Icon(Icons.Filled.Add)
        }
    }

}

@Composable
private fun Tasks(taskState: State<List<Task>?>) {
    taskState.value?.let { tasks ->
        TasksGrid(tasks = tasks, columnCount = 2)
    } ?: run {
        // Else display a lmoading indicator
        // TODO : Shimmer effect ???
        Text(
            text = "Tasks are currently loading",
            style = MaterialTheme.typography.h3
        )
    }
}

@Composable
private fun TasksGrid(
    tasks: List<Task>,
    columnCount: Int
) {
    // Chunk or list to get two task by column
    val tasksChunked = tasks.chunked(columnCount)

    // If we got value display all the task
    LazyColumn {
        this.items(tasksChunked) { taskRow ->
            Row {
                taskRow.forEach { task ->
                    TaskCard(
                        task = task,
                        modifier = Modifier.weight(1f).padding(4.dp)
                    )
                }
            }
        }
    }
}

// TODO : Custom view with it's own view model ?
@Composable
private fun TaskCard(
    task: Task,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
    ) {
        Card {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Text(
                    text = task.name,
                    style = MaterialTheme.typography.h6
                )
                Spacer(modifier = Modifier.padding(4.dp))
                task.entries.forEach { entry ->
                    Text(
                        text = entry.name
                    )
                }
            }
        }
    }
}