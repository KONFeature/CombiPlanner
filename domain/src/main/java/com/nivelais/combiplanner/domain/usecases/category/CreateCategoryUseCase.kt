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
package com.nivelais.combiplanner.domain.usecases.category

import com.nivelais.combiplanner.domain.exceptions.CreateCategoryException
import com.nivelais.combiplanner.domain.repositories.CategoryRepository
import com.nivelais.combiplanner.domain.usecases.core.FlowableUseCase
import kotlinx.coroutines.FlowPreview

/**
 * Use case used to create a new category
 */
class CreateCategoryUseCase(
    private val categoryRepository: CategoryRepository
) : FlowableUseCase<CreateCategoryParams, CreateCategoryResult>() {

    @OptIn(FlowPreview::class)
    override suspend fun execute(params: CreateCategoryParams) {
        log.debug("Creating a new category with param {}", params)
        try {
            val createdCategory = categoryRepository.create(
                params.name,
                params.color
            )
            log.info("New category created {}", createdCategory)
            resultFlow.emit(CreateCategoryResult.SUCCESS)
        } catch (exception: CreateCategoryException) {
            log.warn("Error when creating the category", exception)
            when (exception) {
                is CreateCategoryException.InvalidNameException ->
                    resultFlow.emit(CreateCategoryResult.INVALID_NAME_ERROR)
                is CreateCategoryException.DuplicateNameException ->
                    resultFlow.emit(CreateCategoryResult.DUPLICATE_NAME_ERROR)
            }
        } catch (exception: Throwable) {
            log.error("Unknown error occurred when creating the category", exception)
            resultFlow.emit(CreateCategoryResult.ERROR)
        }
    }
}

/**
 * Input for our use case
 */
data class CreateCategoryParams(
    val name: String,
    val color: Long?
)

/**
 * Possible result of this use case
 */
enum class CreateCategoryResult {
    SUCCESS,
    INVALID_NAME_ERROR,
    DUPLICATE_NAME_ERROR,
    ERROR,
}
