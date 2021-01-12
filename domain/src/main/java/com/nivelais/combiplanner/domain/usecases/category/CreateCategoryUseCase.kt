package com.nivelais.combiplanner.domain.usecases.category

import com.nivelais.combiplanner.domain.exceptions.CreateCategoryException
import com.nivelais.combiplanner.domain.repositories.CategoryRepository
import com.nivelais.combiplanner.domain.usecases.core.FlowableUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview

/**
 * Use case used to create a new category
 */
class CreateCategoryUseCase(
    override val observingScope: CoroutineScope,
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

    override fun initialValue(): CreateCategoryResult =
        CreateCategoryResult.LOADING
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
    LOADING,
    SUCCESS,
    INVALID_NAME_ERROR,
    DUPLICATE_NAME_ERROR,
    ERROR,
}