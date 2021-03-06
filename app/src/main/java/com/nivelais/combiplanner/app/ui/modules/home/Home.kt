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
package com.nivelais.combiplanner.app.ui.modules.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nivelais.combiplanner.R
import com.nivelais.combiplanner.app.ui.modules.category.picker.CategoryPicker
import com.nivelais.combiplanner.app.ui.modules.home.tasks.Tasks
import com.nivelais.combiplanner.app.ui.modules.main.Route
import com.nivelais.combiplanner.app.ui.modules.main.navigate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.compose.get
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun HomePage(
    viewModel: HomeViewModel = getViewModel(),
    navController: NavController = get()
) {
    var filterCardVisibility by remember { viewModel.filterVisibilityState }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        // Bar with the different possible helper
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.home_task_title),
                style = MaterialTheme.typography.h4,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.padding(8.dp))
            // Button used to launch the task creation
            FilterButton(
                onClick = { filterCardVisibility = !filterCardVisibility },
            )
            Spacer(modifier = Modifier.padding(8.dp))
            AddButton(
                onClick = { navController.navigate(Route.Task()) },
            )
        }

        // If the filter card is visible display it
        if (filterCardVisibility) {
            Spacer(modifier = Modifier.padding(8.dp))
            FilterCard {
                // Category picker
                Text(
                    text = stringResource(id = R.string.home_category_title),
                    style = MaterialTheme.typography.h6
                )
                Spacer(modifier = Modifier.padding(8.dp))
                CategoryPicker(
                    onCategoryPicked = {
                        viewModel.selectedCategoryState.value = it
                    }
                )
            }
        }

        Spacer(modifier = Modifier.padding(8.dp))

        // Show the tasks
        Tasks(categoryState = viewModel.selectedCategoryState)
    }
}

/**
 * TODO : Find a way to add a reversed droplet style to the fab when active (displaying the filter card)
 */
@Composable
private fun FilterButton(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        backgroundColor = MaterialTheme.colors.primaryVariant
    ) {
        Icon(Icons.Filled.FilterAlt, "Toggle filter menu")
    }
}

@Composable
private fun AddButton(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        backgroundColor = MaterialTheme.colors.primaryVariant
    ) {
        Icon(Icons.Filled.Add, "Add a task")
    }
}

@Composable
private fun FilterCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = modifier.padding(4.dp)
    ) {
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                content = content
            )
        }
    }
}
