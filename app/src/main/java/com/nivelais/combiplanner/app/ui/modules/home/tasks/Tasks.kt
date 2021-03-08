/*
 * Copyright 2020-2021 Quentin Nivelais
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nivelais.combiplanner.app.ui.modules.home.tasks

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nivelais.combiplanner.R
import com.nivelais.combiplanner.app.di.get
import com.nivelais.combiplanner.app.di.getViewModel
import com.nivelais.combiplanner.app.ui.modules.main.Route
import com.nivelais.combiplanner.app.ui.modules.main.navigate
import com.nivelais.combiplanner.app.ui.widgets.ColorIndicator
import com.nivelais.combiplanner.app.utils.safeItems
import com.nivelais.combiplanner.domain.entities.Category
import com.nivelais.combiplanner.domain.entities.Task
import kotlinx.coroutines.ExperimentalCoroutinesApi

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

    val orientation = LocalConfiguration.current.orientation
    TasksGrid(
        tasks = viewModel.tasks,
        columnCount = if (orientation == Configuration.ORIENTATION_LANDSCAPE) 4 else 2,
        onTaskClicked = { task ->
            navController.navigate(Route.Task(task.id))
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TasksGrid(
    tasks: List<Task>,
    columnCount: Int,
    onTaskClicked: (Task) -> Unit
) {
    // If we got value display all the task
    LazyVerticalGrid(
        cells = GridCells.Fixed(columnCount),
        content = {
            safeItems(tasks) { task ->
                TaskCard(
                    task = task,
                    modifier = Modifier
                        .padding(4.dp)
                        .clickable(
                            onClick = {
                                onTaskClicked(task)
                            }
                        )
                )
            }
        }
    )
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
                style = MaterialTheme.typography.subtitle2
            )
            task.category?.color?.let { colorCode ->
                Spacer(modifier = Modifier.padding(8.dp))
                ColorIndicator(colorCode = colorCode)
            }
        }
        Divider(thickness = 1.dp, modifier = Modifier.padding(0.dp, 8.dp))
        // Entries not done of this task
        task.entries.filter { !it.isDone }.forEach { entry ->
            Text(
                text = entry.name,
                style = MaterialTheme.typography.body2
            )
        }
        // Sum of the entries done
        Divider(thickness = 1.dp, modifier = Modifier.padding(0.dp, 8.dp))
        Text(
            text = stringResource(
                id = R.string.tasks_card_done_state,
                task.entries.filter { it.isDone }.count(),
                task.entries.count()
            ),
            style = MaterialTheme.typography.overline
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
