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

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewModelScope
import com.nivelais.combiplanner.R
import com.nivelais.combiplanner.app.ui.modules.main.GenericViewModel
import com.nivelais.combiplanner.app.utils.runAndCollect
import com.nivelais.combiplanner.domain.usecases.category.CreateCategoryParams
import com.nivelais.combiplanner.domain.usecases.category.CreateCategoryResult
import com.nivelais.combiplanner.domain.usecases.category.CreateCategoryUseCase
import kotlinx.coroutines.Job
import org.koin.core.scope.inject

class CreateCategoryViewModel : GenericViewModel() {

    // View model used to create our scope
    private val createCategoryUseCase: CreateCategoryUseCase by inject()

    // Current name and color picked by the user
    val nameState = mutableStateOf(TextFieldValue())
    val colorState: MutableState<Color?> = mutableStateOf(null)

    // If we got any error in the name put it here
    val nameErrorResState: MutableState<Int?> = mutableStateOf(null)

    // The job that listen on the category creation use case
    private var createCategoryListenerJob: Job? = null

    /**
     * All the different color available
     */
    val colors: List<Color> = listOf(
        Color(0xff2A9D8F),
        Color(0xffE9C46A),
        Color(0xffEB5957),
        Color(0xff33719E),
        Color(0xff9E3C3A),
        Color(0xff239E4A),
        Color(0xff9E691B),
    )

    /**
     * Call to proceed to the category creation
     */
    @OptIn(ExperimentalUnsignedTypes::class)
    fun launchCreation() {
        createCategoryListenerJob?.cancel()
        createCategoryListenerJob = createCategoryUseCase.runAndCollect(
            CreateCategoryParams(
                name = nameState.value.text,
                color = colorState.value?.value?.toLong()
            ),
            viewModelScope
        ) { result ->
            when (result) {
                CreateCategoryResult.SUCCESS -> {
                    // If the creation is in success reset our field
                    nameState.value = TextFieldValue()
                    colorState.value = null
                    nameErrorResState.value = null
                }
                CreateCategoryResult.INVALID_NAME_ERROR -> {
                    // In case of an invalid name error
                    nameErrorResState.value = R.string.create_category_error_invalid_name
                }
                CreateCategoryResult.DUPLICATE_NAME_ERROR -> {
                    // In case of an duplicate name error
                    nameErrorResState.value = R.string.create_category_error_duplicate_name
                }
                CreateCategoryResult.ERROR -> {
                    // In case of an unknown error
                    nameErrorResState.value = R.string.create_category_error
                }
                else -> {
                    // Do nothing in the other case
                }
            }
        }
    }

    override fun clearUseCases() {
        createCategoryListenerJob?.cancel()
        createCategoryUseCase.clear()
    }
}
