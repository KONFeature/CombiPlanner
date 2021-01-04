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

