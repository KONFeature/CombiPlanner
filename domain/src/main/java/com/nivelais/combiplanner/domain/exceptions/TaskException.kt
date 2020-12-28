package com.nivelais.combiplanner.domain.exceptions

/**
 * Exception that can occur when we try to create a task
 */
sealed class CreateTaskException(
    message: String? = null,
    throwable: Throwable? = null
) : Exception(message, throwable) {
    object DuplicateNameException :
        CreateTaskException("Trying to create a task with a task name already existing")

    object InvalidNameException :
        CreateTaskException("The name for the new task is invalid")
}

