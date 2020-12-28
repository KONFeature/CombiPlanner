package com.nivelais.combiplanner.domain.usecases

import com.nivelais.combiplanner.domain.entities.Category
import com.nivelais.combiplanner.domain.repositories.CategoryRepository
import com.nivelais.combiplanner.domain.usecases.core.FlowableUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

/**
 * Use case used to list all the categories we got on the app
 */
class GetCategoriesUseCase(
    override val observingScope: CoroutineScope,
    private val categoryRepository: CategoryRepository
) : FlowableUseCase<Unit, List<Category>?>() {

    private var currentCategoriesFlow: Flow<List<Category>>? = null

    @OptIn(FlowPreview::class)
    override suspend fun execute(params: Unit) {
        log.debug("Listening to all the categories")
        currentCategoriesFlow = categoryRepository.observeAll()
        currentCategoriesFlow?.let {
            resultFlow.emitAll(it)
        }
    }

    override fun initialValue(): List<Category>? = null
}