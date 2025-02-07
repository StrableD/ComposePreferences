package com.strabled.composepreferences.utilis

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import kotlin.math.min

/**
 * A [Composable] function that draws a color wheel.
 *
 * @param modifier A [Modifier] for this [Composable]. Use this to adjust the [androidx.compose.ui.layout.Layout] or add behavior to the color wheel.
 *
 * This function uses [Canvas] to draw a color wheel with a [sweep gradient][Brush.sweepGradient] and a [radial gradient][Brush.radialGradient].
 * The color wheel can be used in various UI components where [Color] selection or display is needed.
 *
 * For more information on [Canvas], see the [Canvas documentation](https://developer.android.com/reference/kotlin/androidx/compose/foundation/Canvas).
 * For more information on [Modifier], see the [Modifier documentation](https://developer.android.com/reference/kotlin/androidx/compose/ui/Modifier).
 */
@Composable
fun ColorWheel(
    modifier: Modifier = Modifier
){

    Canvas(
        modifier = modifier
    ) {
        val size = size
        val radius = min(size.width, size.height) / 2f
        val center = Offset(size.width / 2f, size.height / 2f)

        // Zeichne das Farbrad
        val sweepGradient = Brush.sweepGradient(
            colors = listOf(
                Color.Red,       // 0°
                Color.Yellow,    // 60°
                Color.Green,     // 120°
                Color.Cyan,      // 180°
                Color.Blue,      // 240°
                Color.Magenta,   // 300°
                Color.Red        // 360° oder 0°
            )
        )

        val radialGradient = Brush.radialGradient(
            colors = listOf(Color.White, Color.Transparent),
            center = center,
            radius = radius
        )

        drawCircle(
            brush = sweepGradient,
            center = center,
            radius = radius
        )

        drawCircle(
            brush = radialGradient,
            center = center,
            radius = radius,
            blendMode = BlendMode.Screen
        )
    }
}