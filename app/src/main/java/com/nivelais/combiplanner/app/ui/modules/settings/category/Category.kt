package com.nivelais.combiplanner.app.ui.modules.settings.category

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
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
import com.nivelais.combiplanner.app.ui.modules.category.picker.StatelessCategoriesPicker
import com.nivelais.combiplanner.app.ui.widgets.ColorIndicator
import com.nivelais.combiplanner.domain.entities.Category
import com.nivelais.combiplanner.domain.usecases.category.DeletionStrategy
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun CategoryCard(
    viewModel: CategoryViewModel = CategoryViewModel(),
    category: Category,
    categories: List<Category>
) {
    val errorRes by remember { viewModel.deletionErrorResState }

    CategoryBox {
        // The base content of the category card
        CategoryHeader(
            name = category.name,
            color = category.color,
            onDeleteClick = {
                viewModel.deleteCategory(category = category)
            })
        // If we got an error display it
        errorRes?.let {
            Spacer(modifier = Modifier.padding(8.dp))
            Text(
                text = stringResource(id = it),
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.error
            )
        }

        // If we need to specify a migration strategy show the dialog
        val isStrategyToSpecify by remember { viewModel.isDeletionStrategyRequiredState }
        if (isStrategyToSpecify) {
            MigrationStrategyAlert(
                viewModel = viewModel,
                categories = categories,
                onDismiss = { viewModel.dismissDeletionStrategyDialog() },
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
private fun CategoryHeader(name: String, color: Long?, onDeleteClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.h6,
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
    categories: List<Category>,
    onDismiss: () -> Unit,
    onDeleteClicked: () -> Unit,
) {
    val errorRes by remember { viewModel.deletionErrorResState }
    val isStrategyToSpecify by remember { viewModel.isDeletionStrategyRequiredState }
    var deletionStrategy by remember { viewModel.selectedDeletionStrategyState }

    // TODO : Otpimize that part
    MigrationStrategyAlertBox(
        onDismiss = onDismiss,
        onDeleteClicked = onDeleteClicked
    ) {
        // If we got an error display it
        errorRes?.let {
            Spacer(modifier = Modifier.padding(8.dp))
            Text(
                text = stringResource(id = it),
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.error
            )
        }
        // If we need to specify a migration strategy do it
        if (isStrategyToSpecify) {
            Spacer(modifier = Modifier.padding(8.dp))
            Text(
                text = stringResource(id = R.string.category_deletion_strategy_picker),
                style = MaterialTheme.typography.body2
            )
            Spacer(modifier = Modifier.padding(8.dp))
            DeleteStrategyPicker(
                currentStrategy = deletionStrategy,
                onStrategyPicked = { deletionStrategy = it })
        }
        // If the current strategy is category then show the category picker
        if (isStrategyToSpecify && deletionStrategy is DeletionStrategy.Migrate) {
            // Category picker
            Spacer(modifier = Modifier.padding(8.dp))
            Text(
                text = stringResource(id = R.string.category_categories_picker),
                style = MaterialTheme.typography.body2
            )
            Spacer(modifier = Modifier.padding(8.dp))
            StatelessCategoriesPicker(
                categories = categories,
                categorySelected = viewModel.selectedCategoryState.value,
                onCategoryPicked = {
                    // Update the selected category and the migration target
                    viewModel.selectedCategoryState.value = it
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
    content: @Composable() (ColumnScope.() -> Unit)
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        buttons = {
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(16.dp)
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