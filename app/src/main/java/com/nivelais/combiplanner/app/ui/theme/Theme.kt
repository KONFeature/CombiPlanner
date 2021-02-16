package com.nivelais.combiplanner.app.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun CombiPlannerTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = CombiPlannerColors,
        typography = CombiPlannerTypography,
        shapes = CombiPlannerShapes,
        content = content
    )
}
