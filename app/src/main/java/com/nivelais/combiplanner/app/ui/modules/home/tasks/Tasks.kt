package com.nivelais.combiplanner.app.ui.modules.home.tasks

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.AmbientConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nivelais.combiplanner.R
import com.nivelais.combiplanner.app.ui.modules.main.Route
import com.nivelais.combiplanner.app.ui.modules.main.navigate
import com.nivelais.combiplanner.app.ui.widgets.ColorIndicator
import com.nivelais.combiplanner.app.utils.safeItems
import com.nivelais.combiplanner.domain.entities.Category
import com.nivelais.combiplanner.domain.entities.Task
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.compose.get
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun Tasks(
    navController: NavController = get(),
    categoryState: State<Category?> = mutableStateOf(null),
    viewModel: TasksViewModel = getViewModel()
) {
    // Fetch the task accordingly to the current category selected
    val currentCategory by remember { categoryState }
    viewModel.fetchTasks(category = currentCategory)

    val orientation = AmbientConfiguration.current.orientation
    TasksGrid(
        tasks = viewModel.tasks,
        columnCount = if (orientation == Configuration.ORIENTATION_LANDSCAPE) 4 else 2,
        onTaskClicked = { task ->
            navController.navigate(Route.Task(task.id))
        })
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
        safeItems(items = tasksChunked) { taskRow ->
            Row {
                taskRow.forEach { task ->
                    TaskCard(
                        task = task,
                        modifier = Modifier
                            .weight(1f)
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
        // Header of the card (name and color indicator for category if present)
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
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
        // Entries not done of this task
        task.entries.filter { !it.isDone }.forEach { entry ->
            Text(text = entry.name)
        }
        // Sum of the entries done
        Spacer(modifier = Modifier.padding(4.dp))
        Text(
            text = stringResource(
                id = R.string.tasks_card_done_state,
                task.entries.filter { it.isDone }.count(),
                task.entries.count()
            )
        )
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                content = content
            )
        }
    }
}