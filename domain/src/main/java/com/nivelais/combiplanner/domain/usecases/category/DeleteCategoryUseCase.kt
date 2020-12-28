package com.nivelais.combiplanner.domain.usecases.category

import com.nivelais.combiplanner.domain.repositories.CategoryRepository
import com.nivelais.combiplanner.domain.usecases.core.FlowableUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview

/**
 * Use case used to delete a category
 */
class DeleteCategoryUseCase(
    override val observingScope: CoroutineScope,
    private val categoryRepository: CategoryRepository
) : FlowableUseCase<DeleteCategoryParams, DeleteCategoryResult>() {

    @OptIn(FlowPreview::class)
    override suspend fun execute(params: DeleteCategoryParams) {
        log.debug("Deleting the category with id {}", params.id)
        categoryRepository.delete(params.id)
        resultFlow.emit(DeleteCategoryResult.SUCCESS)
    }

    override fun initialValue(): DeleteCategoryResult =
        DeleteCategoryResult.LOADING
}

/**
 * Input for our use case
 */
data class DeleteCategoryParams(
    val id: Long,
    // TODO : Add params for the task associated delete / migration strategy
)


/**
 * Possible result of this use case
 */
enum class DeleteCategoryResult {
    LOADING,
    SUCCESS
    // TODO : Error dependening on exception that can be throwned by the repository
    // TODO : Throw a state telling some task are associated and need migration / strategy
}