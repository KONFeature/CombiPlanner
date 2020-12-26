package com.nivelais.combiplanner.app.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalUnsignedTypes::class)
@Composable
fun ColorIndicator(
    modifier: Modifier = Modifier,
    size: Dp = 14.dp,
    colorCode: Long,
) {
    Box(
        modifier = modifier
            .size(size = size)
            .background(
                color = Color(value = colorCode.toULong()),
                shape = RoundedCornerShape(100)
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colors.onBackground,
                shape = RoundedCornerShape(100)
            )
    )
}