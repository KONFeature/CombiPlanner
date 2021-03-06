/*
 * Copyright 2020-2021 Quentin Nivelais
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nivelais.combiplanner.app.di

import com.nivelais.combiplanner.data.database.dao.CategoryDao
import com.nivelais.combiplanner.data.database.dao.TaskDao
import com.nivelais.combiplanner.data.database.dao.TaskEntryDao
import com.nivelais.combiplanner.data.database.entities.MyObjectBox
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

    // Repository implementations
    single<CategoryRepository> { CategoryRepositoryImpl(get()) }
    single<TaskRepository> { TaskRepositoryImpl(get(), get()) }
    single<TaskEntryRepository> { TaskEntryRepositoryImpl(get(), get()) }
}
