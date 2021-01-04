package com.nivelais.combiplanner.domain.exceptions

/**
 * Exception that can occur when we try to save a task
 */
sealed class SaveTaskException(
    message: String? = null,
    throwable: Throwable? = null
) : Exception(message, throwable) {
    object DuplicateNameException :
        SaveTaskException("Trying to create a task with a task name already existing")

    object InvalidNameException :
        SaveTaskException("The name for the new task is invalid")
}

