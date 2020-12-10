package com.nivelais.combiplanner.app.di

import com.nivelais.combiplanner.domain.usecases.TestUseCase
import org.koin.dsl.module

/**
 * Inject all the dependency required by our domain layer :
 *  - Use Case
 */
val domainModule = module {

    single { TestUseCase(get()) }

}