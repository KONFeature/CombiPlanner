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
package com.nivelais.combiplanner.data.database

import io.objectbox.Box
import io.objectbox.kotlin.query
import io.objectbox.query.Query
import io.objectbox.query.QueryBuilder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Convert an ObjectBox query to a flow of entities
 */
@OptIn(ExperimentalCoroutinesApi::class)
fun <T> Query<T>.observe(): Flow<List<T>> {
    val subscription = this.subscribe()
    return callbackFlow {
        val observer = subscription.observer { data -> sendBlocking(data) }
        awaitClose { observer.cancel() }
    }
}

/**
 * Observe the result of a specific query
 */
fun <T> Box<T>.observe(block: QueryBuilder<T>.() -> Unit): Flow<List<T>> =
    this.query(block).observe()

/**
 * Observe all the entities of an ObjectBox database
 */
fun <T> Box<T>.observeAll(): Flow<List<T>> = this.query().build().observe()

/**
 * Execute a query and get the result or null
 */
fun <T> Box<T>.getOrNull(block: QueryBuilder<T>.() -> Unit): T? = this.query(block).findFirst()

/**
 * Execute a query and count the result or null
 */
fun <T> Box<T>.count(block: QueryBuilder<T>.() -> Unit): Long = this.query(block).count()
