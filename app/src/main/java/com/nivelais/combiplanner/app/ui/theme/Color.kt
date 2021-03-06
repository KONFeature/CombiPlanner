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
package com.nivelais.combiplanner.app.ui.theme

import androidx.compose.material.darkColors
import androidx.compose.ui.graphics.Color

val PrimaryColor = Color(0xff39e991)
val PrimaryLightColor = Color(0xff67eeaa)
val PrimaryDarkColor = Color(0xff18d777)
val AccentColor = Color(0xffE9C46A)
val BackgroundColor = Color(0xff262833)
val SurfaceColor = Color(0xff3c3f50)

/**
 * Primary : #39e991
 * Primary light : #67eeaa
 * Primary dark : #18d777
 * Background : #262833
 * Card background : #3c3f50
 *
 */

val CombiPlannerColors = darkColors(
    primary = PrimaryColor,
    primaryVariant = PrimaryLightColor,
    secondary = AccentColor,
    error = Color.Red,
    background = BackgroundColor,
    surface = SurfaceColor
)
