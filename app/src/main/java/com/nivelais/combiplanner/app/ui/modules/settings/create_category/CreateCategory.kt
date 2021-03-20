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
package com.nivelais.combiplanner.app.ui.modules.settings.create_category

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.nivelais.combiplanner.R
import org.koin.androidx.compose.getViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun CreateCategory(
    viewModel: CreateCategoryViewModel = getViewModel()
) {
    var categoryName by remember { viewModel.nameState }
    var categoryColor by remember { viewModel.colorState }

    val errorRes by remember { viewModel.nameErrorResState }

    CreateCategoryBox {
        Text(
            text = stringResource(id = R.string.create_category_title),
            style = MaterialTheme.typography.body1,
        )
        Spacer(modifier = Modifier.padding(8.dp))
        NamePicker(
            value = categoryName,
            isError = errorRes != null,
            onValueChange = {
                categoryName = it
            }
        )
        // If we got an error display it
        errorRes?.let {
            Spacer(modifier = Modifier.padding(8.dp))
            Text(
                text = stringResource(id = it),
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.error
            )
        }
        // Color picker
        Spacer(modifier = Modifier.padding(8.dp))
        ColorPicker(
            value = categoryColor,
            onValueChange = {
                categoryColor = it
            },
            colors = viewModel.colors
        )
        // Create button
        Spacer(modifier = Modifier.padding(8.dp))
        Button(
            onClick = {
                viewModel.launchCreation()
            },
            enabled = categoryName.text.isNotBlank(),
        ) {
            Text(
                text = stringResource(id = R.string.create_category_button)
            )
        }
    }
}

@Composable
private fun CreateCategoryBox(
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = Modifier.padding(8.dp),
    ) {
        Card {
            Column(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                content = content
            )
        }
    }
}

@Composable
private fun NamePicker(
    value: TextFieldValue,
    isError: Boolean,
    onValueChange: (TextFieldValue) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        label = {
            Text(text = stringResource(id = R.string.create_category_name_label))
        },
        isError = isError
    )
}

@Composable
private fun ColorPicker(value: Color?, onValueChange: (Color) -> Unit, colors: List<Color>) {
    ColorPickerBox {
        colors.forEach { color ->
            Checkbox(
                checked = value == color,
                onCheckedChange = { isChecked ->
                    // Only send the checked event
                    if (isChecked) onValueChange(color)
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = color,
                    uncheckedColor = color,
                    checkmarkColor = color
                ),
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
private fun ColorPickerBox(
    content: @Composable RowScope.() -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(text = stringResource(id = R.string.create_category_color_label))
        Spacer(modifier = Modifier.padding(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            content = content
        )
    }
}
