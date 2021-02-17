package com.nivelais.combiplanner.app.ui.modules.settings

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nivelais.combiplanner.R
import com.nivelais.combiplanner.app.ui.modules.settings.category.CategoryCard
import com.nivelais.combiplanner.app.ui.modules.settings.create_category.CreateCategory
import com.nivelais.combiplanner.domain.entities.Category
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun SettingsPage(
    viewModel: SettingsViewModel = getViewModel()
) {
    // Categories part of our settings
    Categories(viewModel.categoriesFlow.collectAsState())
}

@Composable
private fun Categories(
    categoriesState: State<List<Category>?>
) {
    categoriesState.value?.let { categories ->
        LazyColumn(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            item {
                Text(
                    text = stringResource(id = R.string.settings_part_category),
                    style = MaterialTheme.typography.h4
                )
                Spacer(modifier = Modifier.padding(8.dp))
            }
            items(items = categories) { category ->
                CategoryCard(category = category, categories = categories)
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