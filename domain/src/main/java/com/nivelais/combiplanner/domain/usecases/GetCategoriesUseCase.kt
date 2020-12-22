package com.nivelais.combiplanner.domain.usecases

import com.nivelais.combiplanner.domain.entities.Category
import com.nivelais.combiplanner.domain.repositories.CategoryRepository
import com.nivelais.combiplanner.domain.usecases.core.FlowableUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.emitAll

/**
 * Use case used to list all the categories we got on the app
 */
class GetCategoriesUseCase(
    override val observingScope: CoroutineScope,
    private val categoryRepository: CategoryRepository
) : FlowableUseCase<Unit, List<Category>?>() {

    @OptIn(FlowPreview::class)
    override suspend fun execute(params: Unit) {
        log.debug("Listening to all the categories")
        val categoriesFlow = categoryRepository.observeAll()
        resultFlow.emitAll(categoriesFlow)
    }

    override fun initialValue(): List<Category>? = null
}