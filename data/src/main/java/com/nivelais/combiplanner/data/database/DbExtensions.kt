package com.nivelais.combiplanner.data.database

import io.objectbox.Box
import io.objectbox.query.Query
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Convert an objectbox query to a flow of entities
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
 * Observe all the entities of an objectbox database
 */
fun <T> Box<T>.observeAll(): Flow<List<T>> = this.query().build().observe();