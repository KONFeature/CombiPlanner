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
package com.nivelais.combiplanner.app.ui.modules.settings

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nivelais.combiplanner.R
import com.nivelais.combiplanner.app.di.getViewModel
import com.nivelais.combiplanner.app.ui.modules.settings.category.CategoryCard
import com.nivelais.combiplanner.app.ui.modules.settings.create_category.CreateCategory
import com.nivelais.combiplanner.app.utils.safeItems
import com.nivelais.combiplanner.domain.entities.Category
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun SettingsPage(
    viewModel: SettingsViewModel = getViewModel()
) {
    // Categories part of our settings
    Categories(viewModel.categories)
}

@Composable
private fun Categories(
    categories: List<Category>
) {
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
        safeItems(items = categories) { category ->
            CategoryCard(category = category, categories = categories)
        }
        item {
            CreateCategory()
        }
    }
}
