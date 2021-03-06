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
package com.nivelais.combiplanner.domain.exceptions

/**
 * Exception that can occur when we try to delete a category
 */
sealed class DeleteCategoryException(
    message: String? = null,
    throwable: Throwable? = null
) : Exception(message, throwable) {
    object TaskAssociatedException :
        DeleteCategoryException(message = "The category has associated tasks.")

    object MigrationIdInvalid :
        DeleteCategoryException(message = "The delete strategy implies a migration of the tasks to another category, but the category id given was invalid.")
}

/**
 * Exception that can occur when we try to create a category
 */
sealed class CreateCategoryException(
    message: String? = null,
    throwable: Throwable? = null
) : Exception(message, throwable) {
    object DuplicateNameException :
        CreateCategoryException("Trying to create a category with a category name already existing")

    object InvalidNameException :
        CreateCategoryException("The name for the new category is invalid")
}
