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
package com.nivelais.combiplanner.app.ui.modules.settings.category

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nivelais.combiplanner.R
import org.koin.androidx.compose.get
import com.nivelais.combiplanner.app.ui.modules.category.picker.StatelessCategoriesPicker
import com.nivelais.combiplanner.app.ui.widgets.ColorIndicator
import com.nivelais.combiplanner.domain.entities.Category
import com.nivelais.combiplanner.domain.usecases.category.DeletionStrategy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun CategoryCard(
    category: Category,
    categories: List<Category>,
    viewModel: CategoryViewModel = get { parametersOf(category, categories) }
) {
    CategoryBox {
        // The base content of the category card
        CategoryHeader(
            name = category.name,
            color = category.color,
            onDeleteClick = {
                viewModel.deleteCategory(category = category)
            }
        )

        // If we need to specify a migration strategy show the dialog
        val isStrategyToSpecify by remember { viewModel.isDeletionStrategyRequiredState }
        if (isStrategyToSpecify) {
            MigrationStrategyAlert(
                viewModel = viewModel,
                onDeleteClicked = {
                    viewModel.deleteCategory(category)
                }
            )
        }
    }
}

@Composable
private fun CategoryBox(
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = Modifier.padding(8.dp),
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

@Composable
private fun CategoryHeader(
    name: String,
    color: Long?,
    onDeleteClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.body1,
            modifier = Modifier.weight(1f)
        )
        color?.let { colorCode ->
            Spacer(modifier = Modifier.padding(8.dp))
            ColorIndicator(colorCode = colorCode)
        }
        Spacer(modifier = Modifier.padding(8.dp))
        // Delete button
        OutlinedButton(onClick = onDeleteClick) {
            Icon(Icons.Filled.DeleteForever, "Delete $name category")
        }
    }
}

/**
 * Alert box displayed when a deletion strategy is required
 */
@Composable
private fun MigrationStrategyAlert(
    viewModel: CategoryViewModel,
    onDeleteClicked: () -> Unit,
) {
    val errorRes by remember { viewModel.deletionErrorResState }
    var deletionStrategy by remember { viewModel.selectedDeletionStrategyState }
    var selectedCategory by remember { viewModel.selectedCategoryState }

    MigrationStrategyAlertBox(
        onDismiss =  { viewModel.dismissDeletionStrategyDialog() },
        onDeleteClicked = onDeleteClicked,
        title = {
            Text(
                text = stringResource(id = R.string.category_deletion_strategy_picker),
                style = MaterialTheme.typography.h6
            )
        }
    ) {
        // If we got an error display it
        errorRes?.let {
            Spacer(modifier = Modifier.padding(8.dp))
            Text(
                text = stringResource(id = it),
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.error
            )
            Spacer(modifier = Modifier.padding(8.dp))
        }
        // If we need to specify a migration strategy do it
        DeleteStrategyPicker(
            currentStrategy = deletionStrategy,
            onStrategyPicked = { deletionStrategy = it }
        )
        // If the current strategy is category then show the category picker
        if (viewModel.isCategoryRequiredForDeletion) {
            // Category picker
            Spacer(modifier = Modifier.padding(8.dp))
            Text(
                text = stringResource(id = R.string.category_categories_picker),
                style = MaterialTheme.typography.subtitle1
            )
            Spacer(modifier = Modifier.padding(8.dp))
            StatelessCategoriesPicker(
                categories = viewModel.categoriesAvailableForMigration,
                categorySelected = selectedCategory,
                onCategoryPicked = {
                    // Update the selected category and the migration target
                    selectedCategory = it
                    (deletionStrategy as DeletionStrategy.Migrate).newCategoryId = it.id
                }
            )
        }
    }
}

/**
 * Alert box displayed when a deletion strategy is required
 */
@Composable
private fun MigrationStrategyAlertBox(
    onDismiss: () -> Unit,
    onDeleteClicked: () -> Unit,
    title: @Composable() (() -> Unit),
    content: @Composable() (ColumnScope.() -> Unit)
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = title,
        buttons = {
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                OutlinedButton(onClick = onDeleteClicked) {
                    Text(
                        text = stringResource(id = R.string.category_deletion_button)
                    )
                }
                Spacer(modifier = Modifier.padding(8.dp))
                Button(onClick = onDismiss) {
                    Text(
                        text = stringResource(id = R.string.category_cancel_button)
                    )
                }
            }
        },
        text = {
            Column(content = content)
        }
    )
}

/**
 * The delete strategy picker
 */
@Composable
private fun DeleteStrategyPicker(
    currentStrategy: DeletionStrategy?,
    onStrategyPicked: (DeletionStrategy) -> Unit
) {
    // The flow row of our categories
    Column(
        modifier = Modifier.padding(8.dp),
    ) {
        DeletionStrategyCheckbox(
            textRes = R.string.category_deletion_strategy_migrate_to_null,
            isChecked = currentStrategy is DeletionStrategy.MigrateToNull,
            onCheckedChange = {
                if (it) onStrategyPicked(DeletionStrategy.MigrateToNull)
            }
        )
        Spacer(modifier = Modifier.padding(8.dp))
        DeletionStrategyCheckbox(
            textRes = R.string.category_deletion_strategy_cascade,
            isChecked = currentStrategy is DeletionStrategy.Cascade,
            onCheckedChange = {
                if (it) onStrategyPicked(DeletionStrategy.Cascade)
            }
        )
        Spacer(modifier = Modifier.padding(8.dp))
        DeletionStrategyCheckbox(
            textRes = R.string.category_deletion_strategy_migrate,
            isChecked = currentStrategy is DeletionStrategy.Migrate,
            onCheckedChange = {
                if (it) onStrategyPicked(DeletionStrategy.Migrate(0L))
            }
        )
    }
}

@Composable
private fun DeletionStrategyCheckbox(
    textRes: Int,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { onCheckedChange(!isChecked) }
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange
        )
        Spacer(modifier = Modifier.padding(8.dp))
        Text(text = stringResource(id = textRes))
    }
}
