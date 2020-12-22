package com.nivelais.combiplanner.app.ui.modules.settings.create_category

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.nivelais.combiplanner.R
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun CreateCategory(
    viewModel: CreateCategoryViewModel = getViewModel()
) {
    var categoryName by remember { viewModel.name }
    var categoryColor by remember { viewModel.color }

    val creationState = viewModel.creationFlow.collectAsState()

    CreateCategoryBox {
        Text(
            text = stringResource(id = R.string.create_category_title),
            style = MaterialTheme.typography.h6
        )
        Spacer(modifier = Modifier.padding(8.dp))
        NamePicker(
            value = categoryName,
            onValueChange = {
                categoryName = it
            })
        Spacer(modifier = Modifier.padding(8.dp))
        ColorPicker(
            value = categoryColor,
            onValueChange = {
                categoryColor = it
            },
            colors = viewModel.colors
        )
        Spacer(modifier = Modifier.padding(8.dp))
        OutlinedButton(
            onClick = {
                viewModel.launchCreation()
            }) {
            Text(
                text = stringResource(id = R.string.create_category_button)
            )
        }
        Spacer(modifier = Modifier.padding(4.dp))
        Text(
            text = "Creation step = ${creationState.value}"
        )
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
private fun NamePicker(value: TextFieldValue, onValueChange: (TextFieldValue) -> Unit) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        label = {
            Text(text = stringResource(id = R.string.create_category_name_label))
        }
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