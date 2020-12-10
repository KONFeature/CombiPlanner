package com.nivelais.combiplanner.app.di

import com.nivelais.combiplanner.data.repositories.TestRepositoryImpl
import com.nivelais.combiplanner.domain.repositories.TestRepository
import org.koin.dsl.module

/**
 * Inject all the dependency required by our data layer :
 *  - Network
 *  - Database
 *  - Repository
 */
val dataModule = module {

    single<TestRepository> { TestRepositoryImpl() }

}