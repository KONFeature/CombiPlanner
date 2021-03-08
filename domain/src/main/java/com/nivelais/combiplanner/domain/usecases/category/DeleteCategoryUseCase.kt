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

import com.nivelais.combiplanner.domain.exceptions.DeleteCategoryException
import com.nivelais.combiplanner.domain.repositories.CategoryRepository
import com.nivelais.combiplanner.domain.repositories.TaskRepository
import com.nivelais.combiplanner.domain.usecases.core.FlowableUseCase
import kotlinx.coroutines.FlowPreview

/**
 * Use case used to delete a category
 */
class DeleteCategoryUseCase(
    private val categoryRepository: CategoryRepository,
    private val taskRepository: TaskRepository
) : FlowableUseCase<DeleteCategoryParams, DeleteCategoryResult>() {

    @OptIn(FlowPreview::class)
    override suspend fun execute(params: DeleteCategoryParams) {
        log.debug("Deleting the category with id {}", params.id)

        try {
            // Check if we got a migration strategy
            params.strategy?.let { strategy ->
                if (strategy is DeletionStrategy.Cascade) {
                    // Remove all the tasks
                    taskRepository.deleteForCategory(params.id)
                } else if (strategy is DeletionStrategy.Migrate) {
                    // Migrate the tasks to another category
                    categoryRepository.migrate(params.id, strategy.newCategoryId)
                }
            }

            // Then just perform the delete
            categoryRepository.delete(params.id)
            resultFlow.emit(DeleteCategoryResult.SUCCESS)
        } catch (exception: DeleteCategoryException) {
            log.warn("Error when deleting the category", exception)
            when (exception) {
                is DeleteCategoryException.TaskAssociatedException ->
                    resultFlow.emit(DeleteCategoryResult.STRATEGY_REQUIRED)
                is DeleteCategoryException.MigrationIdInvalid ->
                    resultFlow.emit(DeleteCategoryResult.INVALID_TARGET_CATEGORY)
            }
        } catch (exception: Throwable) {
            log.error(
                "An unknown error occurred during the deletion of the category with id {} and strategy {}",
                params.id, params.strategy, exception
            )
            resultFlow.emit(DeleteCategoryResult.ERROR)
        }
    }
}

/**
 * Input for our use case
 */
data class DeleteCategoryParams(
    val id: Long,
    val strategy: DeletionStrategy? = null
)

sealed class DeletionStrategy {
    object MigrateToNull : DeletionStrategy()
    object Cascade : DeletionStrategy()
    data class Migrate(
        var newCategoryId: Long
    ) : DeletionStrategy()
}

/**
 * Possible result of this use case
 */
enum class DeleteCategoryResult {
    SUCCESS,
    STRATEGY_REQUIRED,
    INVALID_TARGET_CATEGORY,
    ERROR
}
