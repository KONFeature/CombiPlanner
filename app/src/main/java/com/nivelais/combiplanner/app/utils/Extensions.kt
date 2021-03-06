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
package com.nivelais.combiplanner.app.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import com.nivelais.combiplanner.domain.usecases.core.FlowableUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

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

/**
 * Convert a bitmap to a byte array, with it's extensions
 */
fun Bitmap.toBytes(): Pair<ByteArray, String> {
    val outputStream = ByteArrayOutputStream()
    val format = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        compress(Bitmap.CompressFormat.WEBP_LOSSLESS, 20, outputStream)
        "webp"
    } else {
        compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        "png"
    }
    return outputStream.toByteArray() to format
}

/**
 * Convert a byte array to a bitmap object
 */
fun ByteArray.toBitmap(): Bitmap = BitmapFactory.decodeByteArray(this, 0, size)
