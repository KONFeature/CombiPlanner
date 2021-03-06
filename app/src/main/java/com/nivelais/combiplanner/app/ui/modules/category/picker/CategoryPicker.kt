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
package com.nivelais.combiplanner.app.ui.modules.category.picker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nivelais.combiplanner.app.di.getViewModel
import com.nivelais.combiplanner.app.ui.widgets.ColorIndicator
import com.nivelais.combiplanner.app.ui.widgets.SimpleFlowRow
import com.nivelais.combiplanner.domain.entities.Category
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.core.parameter.parametersOf

/**
 * Widget to perform category filtering
 */
@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun CategoryPicker(
    viewModel: CategoryPickerViewModel = getViewModel { parametersOf(initialCategory) },
    initialCategory: Category? = null,
    onCategoryPicked: (Category?) -> Unit
) {
    // The flow row of our categories
    StatelessCategoriesPicker(
        categories = viewModel.categories,
        categorySelected = viewModel.selectedCategoryState.value,
        onCategoryPicked = {
            viewModel.onCategorySelected(it)
            onCategoryPicked(viewModel.selectedCategoryState.value)
        }
    )
}

@Composable
fun StatelessCategoriesPicker(
    categories: List<Category>,
    categorySelected: Category?,
    onCategoryPicked: (Category) -> Unit
) {
    // The flow row of our categories
    SimpleFlowRow(
        verticalGap = 8.dp,
        horizontalGap = 8.dp,
        alignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        categories.forEach { category ->
            Row {
                CategorySelectableCard(
                    category = category,
                    isSelected = category == categorySelected,
                    onClick = {
                        onCategoryPicked(category)
                    }
                )
            }
        }
    }
}

@Composable
private fun CategorySelectableCard(
    category: Category,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val cardColor = if (isSelected) {
        MaterialTheme.colors.primary
    } else {
        MaterialTheme.colors.surface
    }

    Card(
        shape = MaterialTheme.shapes.small,
        backgroundColor = cardColor,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = category.name,
                modifier = Modifier.padding(4.dp)
            )
            // If this category got a color display it
            category.color?.let { colorCode ->
                ColorIndicator(colorCode = colorCode)
            }
        }
    }
}
