package com.nivelais.combiplanner.app.ui.modules.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.onActive
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nivelais.combiplanner.R
import com.nivelais.combiplanner.app.ui.modules.settings.create_category.CreateCategory
import com.nivelais.combiplanner.app.ui.widgets.ColorIndicator
import com.nivelais.combiplanner.domain.entities.Category
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun SettingsPage(
    viewModel: SettingsViewModel = getViewModel()
) {
    onActive {
        // Launch the categories fetching
        viewModel.fetchCategories()
    }

    // Categories part of our settings
    Categories(viewModel.categoriesFlow.collectAsState()) { category ->
        viewModel.deleteCategory(category = category)
    }
}

@Composable
private fun Categories(
    categoriesState: State<List<Category>?>,
    onCategoryDelete: (Category) -> Unit
) {
    categoriesState.value?.let { categories ->
        LazyColumn(
            modifier = Modifier.padding(16.dp).fillMaxWidth()
        ) {
            item {
                Text(
                    text = stringResource(id = R.string.settings_part_category),
                    style = MaterialTheme.typography.h4
                )
            }
            items(categories) { category ->
                CategoryCard(category = category) {
                    // Callback on click of the delete button
                    onCategoryDelete(category)
                }
            }
            item {
                CreateCategory()
            }
        }
    } ?: run {
        Text(
            text = stringResource(id = R.string.settings_part_category_loading),
            style = MaterialTheme.typography.h4
        )
    }
}

@Composable
private fun CategoryCard(category: Category, onDelete: () -> Unit) {
    Box(
        modifier = Modifier.padding(8.dp),
    ) {
        Card {
            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.weight(1f)
                )
                category.color?.let { colorCode ->
                    Spacer(modifier = Modifier.padding(8.dp))
                    ColorIndicator(colorCode = colorCode)
                }
                Spacer(modifier = Modifier.padding(8.dp))
                // TODO : Warning, delete also delete all the task associated, propose task category migration ??
                OutlinedButton(onClick = { onDelete() }) {
                    Icon(Icons.Filled.DeleteForever)
                }
            }
        }
    }
}