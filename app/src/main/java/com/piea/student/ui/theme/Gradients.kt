package com.piea.student.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * Shared premium gradients used across hero sections, headers, and cards
 * to give the app a richer, more polished look.
 */
object PieaGradients {
    val PrimaryHero = Brush.linearGradient(
        colors = listOf(PieaBrown, PieaBrownDark)
    )

    val GoldAccent = Brush.linearGradient(
        colors = listOf(PieaGold, PieaGoldLight)
    )

    val SplashBackground = Brush.verticalGradient(
        colors = listOf(PieaBrownDark, PieaBrown, Color(0xFF7A4F2E))
    )

    val AuthHeader = Brush.linearGradient(
        colors = listOf(PieaBrownDark, PieaBrown, PieaGold)
    )

    val CardShimmer = Brush.linearGradient(
        colors = listOf(PieaCream, PieaSurfaceLight)
    )
}
