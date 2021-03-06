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

import com.nivelais.combiplanner.domain.entities.Category
import com.nivelais.combiplanner.domain.repositories.CategoryRepository
import com.nivelais.combiplanner.domain.usecases.core.SimpleFlowUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow

/**
 * Use case used to list all the categories we got on the app
 */
class GetCategoriesUseCase(
    override val observingScope: CoroutineScope,
    private val categoryRepository: CategoryRepository
) : SimpleFlowUseCase<Unit, List<Category>>() {

    @OptIn(FlowPreview::class)
    override fun execute(params: Unit): Flow<List<Category>> {
        log.debug("Listening to all the categories")
        return categoryRepository.observeAll()
    }
}
