package com.nivelais.combiplanner.app.utils

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import com.nivelais.combiplanner.domain.usecases.core.FlowableUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


/**
 * Safely adds a list of items. (Useful for the snapshot state list that can contain null element sometimes)
 *
 * @param items the data list
 * @param itemContent the content displayed by a single item
 */
inline fun <T> LazyListScope.safeItems(
    items: List<T>,
    crossinline itemContent: @Composable LazyItemScope.(item: T) -> Unit
) = items(items.size) { index ->
    items.getOrNull(index)?.let { itemContent(it) }
}

/**
 * Run a given use case and collect the result in the given coroutine scope
 */
inline fun <In, Out> FlowableUseCase<In, Out>.runAndCollect(
    input: In,
    scope: CoroutineScope,
    crossinline action: suspend (value: Out) -> Unit
): Job {
    val flow = this.flow

    val collectJob = scope.launch {
        flow.collect(action)
    }
    run(input)

    return collectJob
}