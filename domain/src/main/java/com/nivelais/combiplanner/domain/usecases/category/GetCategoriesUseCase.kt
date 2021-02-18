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