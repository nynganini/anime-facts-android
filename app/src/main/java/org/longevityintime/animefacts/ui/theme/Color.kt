package org.longevityintime.animefacts.ui.theme

import androidx.compose.material.Colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)

val PrimaryColor = Color(0XFF0C1E3C)
val PrimaryDarkColor = Color(0XFF041E42)
val SecondaryColor = Color(0XFF21355C)
val AccentColor = Color(0XFF3683FC)

@Composable
fun Colors.compositedOnSurface(alpha: Float): Color {
    return onSurface.copy(alpha = alpha).compositeOver(surface)
}