package com.nivelais.combiplanner.app.di

import com.nivelais.combiplanner.data.database.dao.CategoryDao
import com.nivelais.combiplanner.data.database.dao.PictureDao
import com.nivelais.combiplanner.data.database.dao.TaskDao
import com.nivelais.combiplanner.data.database.dao.TaskEntryDao
import com.nivelais.combiplanner.data.database.entities.MyObjectBox
import com.nivelais.combiplanner.data.database.mapper.CategoryDatabaseMapper
import com.nivelais.combiplanner.data.repositories.CategoryRepositoryImpl
import com.nivelais.combiplanner.data.repositories.TaskEntryRepositoryImpl
import com.nivelais.combiplanner.data.repositories.TaskRepositoryImpl
import com.nivelais.combiplanner.domain.repositories.CategoryRepository
import com.nivelais.combiplanner.domain.repositories.TaskEntryRepository
import com.nivelais.combiplanner.domain.repositories.TaskRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Inject all the dependency required by our data layer :
 *  - Network
 *  - Database
 *  - Repository
 */
val dataModule = module(
    createdAtStart = true
) {

    // Init ObjectBox
    single {
        MyObjectBox.builder()
            .name("CombiPlannerDatabase")
            .androidContext(androidContext())
            .build()
    }

    // DAO's
    single { CategoryDao(get()) }
    single { TaskDao(get()) }
    single { TaskEntryDao(get()) }
    single { PictureDao(get()) }

    // Repository implementations
    single<CategoryRepository> { CategoryRepositoryImpl(get()) }
    single<TaskRepository> { TaskRepositoryImpl(get(), get()) }
    single<TaskEntryRepository> { TaskEntryRepositoryImpl(get(), get()) }

}