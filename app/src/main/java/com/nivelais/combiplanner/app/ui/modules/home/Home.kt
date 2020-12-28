package com.nivelais.combiplanner.app.ui.modules.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Filter2
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import com.nivelais.combiplanner.R
import com.nivelais.combiplanner.app.ui.modules.home.tasks.Tasks
import com.nivelais.combiplanner.app.ui.modules.main.Routes
import com.nivelais.combiplanner.app.ui.widgets.CategoryPicker
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun HomePage(
    viewModel: HomeViewModel = getViewModel(),
    navController: NavController
) {
    onActive {
        viewModel.fetchCategories()
    }

    var filterCardVisibility by remember { viewModel.filterVisibilityState }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp).fillMaxWidth()
    ) {
        Text(
            text = "Tasks",
            style = MaterialTheme.typography.h2
        )
        // Bar with the different possible helper
        Row {
            // Button used to launch the task creation
            FilterButton(
                onClick = { filterCardVisibility = !filterCardVisibility },
            )
            Spacer(modifier = Modifier.padding(8.dp))
            AddButton(
                onClick = { navController.navigate(Routes.CREATE_TASK) },
            )
        }

        // If the filter card is visible display it
        if (filterCardVisibility) {
            FilterCard {
                // Category picker
                Text(
                    text = stringResource(id = R.string.home_category_title),
                    style = MaterialTheme.typography.body2
                )
                Spacer(modifier = Modifier.padding(8.dp))
                val categoriesState = viewModel.categoriesFlow.collectAsState()
                categoriesState.value?.let { categories ->
                    CategoryPicker(
                        categories = categories,
                        categorySelected = viewModel.selectedCategoryState.value,
                        onCategoryPicked = {
                            if(viewModel.selectedCategoryState.value == it) {
                                viewModel.selectedCategoryState.value = it
                            }
                        })
                }
            }
        }

        Spacer(modifier = Modifier.padding(8.dp))

        // Show the tasks
        Tasks(navController = navController, category = viewModel.selectedCategoryState)
    }
}

@Composable
private fun FilterButton(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
    ) {
        Icon(Icons.Filled.FilterAlt)
    }
}

@Composable
private fun AddButton(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
    ) {
        Icon(Icons.Filled.Add)
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
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                content = content
            )
        }
    }
}