package com.nivelais.combiplanner.app.utils

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable


/**
 * Safely adds a list of items. (Usefull for the snapshot state list that can contain null element sometimes)
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