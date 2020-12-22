package com.nivelais.combiplanner.domain.usecases

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
        val createdCategory = categoryRepository.create(
            params.name,
            params.color
        )
        log.info("New category created {}", createdCategory)
        resultFlow.emit(CreateCategoryResult.SUCCESS)
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
    SUCCESS
    // TODO : Error dependening on exception that can be throwned by the repository
}