package com.nivelais.combiplanner.app.ui.modules.main

import androidx.lifecycle.ViewModel
import com.nivelais.combiplanner.domain.common.logger
import org.koin.core.scope.KoinScopeComponent
import org.koin.core.scope.Scope
import org.koin.core.scope.newScope

/**
 * Generic view model binded to a Koin Scope
 */
abstract class GenericViewModel : ViewModel(), KoinScopeComponent {

    // Logger for all of our view model
    val log by logger

    // The scope for this view model
    override val scope: Scope by lazy { newScope(this) }

    // Abstract function that will close all the use case
    abstract fun clearUseCases()

    /**
     * When view model cleared close the scope
     */
    override fun onCleared() {
        log.info("Clearing this view model, and so the use case and the koin scope")
        super.onCleared()
        clearUseCases()
        closeScope()
    }
}