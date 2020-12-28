package com.nivelais.combiplanner.app.ui.modules.home.tasks

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import com.nivelais.combiplanner.app.ui.modules.main.Routes
import com.nivelais.combiplanner.app.ui.widgets.ColorIndicator
import com.nivelais.combiplanner.domain.entities.Category
import com.nivelais.combiplanner.domain.entities.Task
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun Tasks(
    navController: NavController,
    category: State<Category?> = mutableStateOf(null),
    viewModel: TasksViewModel = getViewModel()
) {
    onActive {
        // Launch the task fetching
        viewModel.fetchTasks()
    }

    category.value?.let {
        viewModel.fetchTasks(it)
    }


    val tasksState = viewModel.tasksFlow.collectAsState()

    tasksState.value?.let { tasks ->
        TasksGrid(tasks = tasks, columnCount = 2, onTaskClicked = { task ->
            navController.navigate(Routes.TASK + task.id)
        })
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
    columnCount: Int,
    onTaskClicked: (Task) -> Unit
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
                        modifier = Modifier.weight(1f)
                            .padding(4.dp)
                            .clickable(onClick = {
                                onTaskClicked(task)
                            })
                    )
                }
            }
        }
    }
}

@Composable
private fun TaskCard(
    task: Task,
    modifier: Modifier
) {
    TaskCardBox(
        modifier = modifier
    ) {
        Row {
            Text(
                modifier = modifier.weight(1f),
                text = task.name,
                style = MaterialTheme.typography.h6
            )
            task.category.color?.let { colorCode ->
                Spacer(modifier = Modifier.padding(8.dp))
                ColorIndicator(colorCode = colorCode)
            }
        }
        Spacer(modifier = Modifier.padding(4.dp))
        task.entries.forEach { entry ->
            Text(
                text = entry.name
            )
        }
    }
}

@Composable
private fun TaskCardBox(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = modifier
    ) {
        Card {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                content = content
            )
        }
    }
}