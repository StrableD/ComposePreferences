package com.strabled.composepreferences.utilis

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

/**
 * A [Composable] function that displays a [ColorWheel] picker.
 *
 * This function allows users to select a [Color] from a [ColorWheel]. The selected [Color] can be adjusted
 * using a [VerticalValueSlider] to [blend][blendColor] the [Color] with [black][Color.Black] or [white][Color.White]. The function provides visual
 * feedback with an [indicator][ColorPickerIndicator] that shows the current selection.
 *
 * @param modifier [Modifier] to be applied to the [ColorWheel] picker.
 * @param initialColor The initial [Color] to be selected.
 * @param showIndicator Whether to show the [color indicator][ColorPickerIndicator].
 * @param onColorSelected Callback function to be invoked when a [Color] is selected.
 */
@Composable
fun ColorWheelPicker(
    modifier: Modifier = Modifier.height(220.dp),
    initialColor: Color = Color.White,
    showIndicator: Boolean = true,
    onColorSelected: (Color) -> Unit
) {
    var fingerPosition by remember { mutableStateOf(Offset.Unspecified) }
    var currentlyDragging by remember { mutableStateOf(false) }
    var selectedColor by remember { mutableStateOf(initialColor) } // Farbe des Kreises
    var sliderPosition by remember { mutableFloatStateOf(0.5f) } // Slider-Position von 0f bis 1f
    var outputColor by remember { mutableStateOf(initialColor) } // Ergebnisfarbe
    var radius by remember { mutableStateOf(0f) }
    var center by remember { mutableStateOf(Offset.Zero) }

    // Rechne die Ergebnisfarbe neu, wenn sich selectedColor oder sliderPosition ändert
    LaunchedEffect(selectedColor, sliderPosition) {
        outputColor = blendColor(selectedColor, sliderPosition)
        onColorSelected(outputColor)
    }

    Row(modifier = modifier.padding(8.dp)) {
        Box(
            modifier = Modifier
                .size(200.dp)
        ) {
            ColorWheel(
                modifier = Modifier
                    .size(270.dp)
                    .clip(CircleShape)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = { offset ->
                                val dx = offset.x - center.x
                                val dy = offset.y - center.y
                                val distance = hypot(dx, dy)

                                if (distance <= radius) {
                                    val angle = atan2(dy, dx)
                                    val hue = ((angle.toDegrees() + 360) % 360).toFloat()
                                    val saturation = (distance / radius).coerceIn(0f, 1f)
                                    selectedColor = Color.hsv(hue, saturation, 1f)
                                    fingerPosition = offset
                                    sliderPosition = 0.5f // Slider auf Mitte zurücksetzen
                                }
                            }
                        )
                    }
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = {
                                currentlyDragging = true
                            },
                            onDragEnd = {
                                currentlyDragging = false
                            },
                            onDragCancel = {
                                currentlyDragging = false
                            },
                            onDrag = { change, off ->
                                fingerPosition += off
                                val dx = fingerPosition.x - center.x
                                val dy = fingerPosition.y - center.y
                                val distance = hypot(dx, dy)

                                val angle = atan2(dy, dx)
                                val hue = ((angle.toDegrees() + 360) % 360).toFloat()
                                val saturation = (distance / radius).coerceIn(0f, 1f)
                                selectedColor = Color.hsv(hue, saturation, 1f)
                                sliderPosition = 0.5f // Slider auf Mitte zurücksetzen
                            }
                        )
                    }
                    .onPlaced {
                        radius = it.size.width / 2f
                        center = Offset(it.size.width / 2f, it.size.height / 2f)
                        if (fingerPosition == Offset.Unspecified) {
                            val hsv = selectedColor.toHSV()
                            val hue = hsv[0]
                            val saturation = hsv[1]
                            fingerPosition = calculateOffsetFromHSV(hue, saturation, radius, center)
                        }
                    }
            )

            if (showIndicator) {
                ColorPickerIndicator(
                    position = fingerPosition.coerceInRadius(radius, center),
                    color = selectedColor,
                    open = currentlyDragging,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        VerticalValueSlider(
            modifier = Modifier
                .width(40.dp)
                .fillMaxHeight(),
            selectedColor = selectedColor,
            sliderPosition = sliderPosition,
            outputColor = outputColor,
            onValueChanged = { newPosition ->
                sliderPosition = newPosition.coerceIn(0f, 1f)
            },
            showIndicator = showIndicator
        )
    }
}

/**
 * A [Composable] function that displays a vertical value slider.
 *
 * This function allows users to adjust the brightness of the selected [Color] by blending it with [black][Color.Black] or [white][Color.White].
 * The slider provides visual feedback with an [indicator][SliderIndicator] that shows the current blend position.
 *
 * @param modifier Modifier to be applied to the slider.
 * @param selectedColor The currently selected color.
 * @param sliderPosition The current position of the slider.
 * @param outputColor The resulting color after blending.
 * @param onValueChanged Callback function to be invoked when the slider value changes.
 * @param showIndicator Whether to show the slider indicator.
 */
@Composable
fun VerticalValueSlider(
    modifier: Modifier = Modifier,
    selectedColor: Color,
    sliderPosition: Float,
    outputColor: Color,
    onValueChanged: (Float) -> Unit,
    showIndicator: Boolean = true
) {
    var sliderHeight by remember { mutableFloatStateOf(0f) }
    var indicatorPosition by remember { mutableStateOf(Offset.Zero) }

    Box(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 6.dp)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDrag = { change, _ ->
                            val offset = change.position
                            val y = offset.y.coerceIn(0f, sliderHeight)
                            val newPosition = y / sliderHeight
                            onValueChanged(newPosition)
                            indicatorPosition = Offset(size.width / 2f, y)
                        },
                        onDragStart = { offset ->
                            val y = offset.y.coerceIn(0f, sliderHeight)
                            val newPosition = y / sliderHeight
                            onValueChanged(newPosition)
                            indicatorPosition = Offset(size.width / 2f, y)
                        },
                        onDragEnd = { },
                        onDragCancel = { }
                    )
                }
        ) {
            sliderHeight = size.height
            // Vertikaler Farbverlauf von Schwarz über die ausgewählte Farbe bis Weiß
            val gradientBrush = Brush.verticalGradient(
                colors = listOf(
                    Color.Black,         // Oben
                    selectedColor,       // Mitte
                    Color.White          // Unten
                ),
                startY = 0f,
                endY = size.height
            )

            // Zeichne den Slider mit PillShape
            drawRoundRect(
                brush = gradientBrush,
                size = size,
                cornerRadius = CornerRadius(size.width / 2f, size.width / 2f)
            )

            // Aktualisiere die Position des Indikators basierend auf der aktuellen Position
            val y = sliderPosition * size.height
            indicatorPosition = Offset(size.width / 2f, y)
        }

        if (showIndicator) {
            SliderIndicator(
                position = indicatorPosition,
                color = outputColor, // Zeigt die Ergebnisfarbe an
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

/**
 * Blends the selected [Color] with [black][Color.Black] or [white][Color.White] based on the slider position.
 *
 * This function calculates the resulting [Color] by linearly interpolating between the [selected color][selectedColor] and [black][Color.Black] or [white][Color.White],
 * depending on the [position][sliderPosition] of the slider.
 *
 * @param selectedColor The currently selected color.
 * @param sliderPosition The current position of the slider.
 * @return The blended color.
 */
fun blendColor(selectedColor: Color, sliderPosition: Float): Color {
    return when {
        sliderPosition < 0.5f -> {
            // Blend Richtung Schwarz
            val factor = sliderPosition / 0.5f
            blendColors(Color.Black, selectedColor, factor)
        }

        sliderPosition > 0.5f -> {
            // Blend Richtung Weiß
            val factor = (sliderPosition - 0.5f) / 0.5f
            blendColors(selectedColor, Color.White, factor)
        }

        else -> {
            // sliderPosition == 0.5f
            selectedColor
        }
    }
}

/**
 * Linearly interpolates between two [colors][Color].
 *
 * This function calculates the intermediate [Color] by linearly interpolating between two given [colors][Color]
 * based on a specified [factor].
 *
 * @param color1 The first [Color].
 * @param color2 The second [Color].
 * @param factor The interpolation factor (0.0 to 1.0).
 * @return The interpolated [Color].
 */
fun blendColors(color1: Color, color2: Color, factor: Float): Color {
    val r = color1.red + (color2.red - color1.red) * factor
    val g = color1.green + (color2.green - color1.green) * factor
    val b = color1.blue + (color2.blue - color1.blue) * factor
    val a = color1.alpha + (color2.alpha - color1.alpha) * factor
    return Color(r, g, b, a)
}

/**
 * Converts a [Color] to its [HSV](https://en.wikipedia.org/wiki/HSL_and_HSV) (Hue, Saturation, Value) representation.
 *
 * This function converts the [Color] from the [ARGB][androidx.compose.ui.graphics.colorspace.ColorSpaces.Srgb] color space to the [HSV](https://en.wikipedia.org/wiki/HSL_and_HSV) color space and returns the [HSV](https://en.wikipedia.org/wiki/HSL_and_HSV) values.
 *
 * @return A float array containing the [HSV](https://en.wikipedia.org/wiki/HSL_and_HSV) values.
 */
fun Color.toHSV(): FloatArray {
    val red = this.red / 255
    val green = this.green / 255
    val blue = this.blue / 255
    val value = maxOf(red, green, blue)
    val min = minOf(red, green, blue)
    val dif = value - min
    val sat = if (value == 0f) 0f else dif / value
    val hue = when (value) {
        min -> 0f
        red -> ((green - blue) / dif) % 6
        green -> (blue - red) / dif + 2
        else -> (red - green) / dif + 4
    }
    return FloatArray(3).apply {
        this[0] = hue
        this[1] = sat
        this[2] = value
    }
}

/**
 * Calculates the [Offset] position from [HSV](https://en.wikipedia.org/wiki/HSL_and_HSV) values.
 *
 * This function calculates the position on the [ColorWheel] based on the given [hue] and [saturation] values.
 *
 * @param hue The hue value.
 * @param saturation The saturation value.
 * @param radius The radius of the color wheel.
 * @param center The center of the color wheel.
 * @return The offset position.
 */
fun calculateOffsetFromHSV(hue: Float, saturation: Float, radius: Float, center: Offset): Offset {
    val angleInRadians = hue.toRadians()
    val distance = saturation * radius
    val x = center.x + (distance * cos(angleInRadians)).toFloat()
    val y = center.y + (distance * sin(angleInRadians)).toFloat()
    return Offset(x, y)
}

/**
 * A [Composable] function that displays a color picker indicator.
 *
 * This function provides a visual indicator for the [color picker][ColorWheelPicker], showing the current selected [Color] and [position].
 * The indicator can animate its size and position based on user interaction.
 *
 * @param position The position of the indicator.
 * @param color The color of the indicator.
 * @param open Whether the indicator is open.
 * @param modifier Modifier to be applied to the indicator.
 */
@Composable
fun ColorPickerIndicator(
    position: Offset,
    color: Color,
    open: Boolean,
    modifier: Modifier = Modifier
) {
    val animatedPickerRadius by animateDpAsState(if (open) 30.dp else 2.dp, tween(250), label = "radiusAnimation")
    val animatedPickerOffset by animateDpAsState(if (open) 10.dp else 0.dp, tween(250), label = "offsetAnimation")
    Canvas(modifier = modifier.width(270.dp)) {
        val pickerRadius = animatedPickerRadius.toPx()
        val offsetY = position.y - pickerRadius - animatedPickerOffset.toPx()
        val adjustedOffset = Offset(position.x, offsetY)

        // Begrenze den Indikator innerhalb des Farbrads
        val clampedOffset = Offset(
            x = adjustedOffset.x.coerceIn(pickerRadius, size.width - pickerRadius),
            y = adjustedOffset.y.coerceIn(pickerRadius, size.height - pickerRadius)
        )

        // Hintergrund
        drawCircle(
            color = Color.White,
            center = clampedOffset,
            radius = pickerRadius + 4.dp.toPx(),
            alpha = 0.8f
        )

        // Rahmen
        drawCircle(
            color = Color.Gray,
            center = clampedOffset,
            radius = pickerRadius + 2.dp.toPx(),
            style = Stroke(width = 2.dp.toPx())
        )

        // Zentrum mit der aktuellen Farbe
        drawCircle(
            color = color,
            center = clampedOffset,
            radius = pickerRadius
        )
    }
}

/**
 * Coerces the [Offset] within the [radius] around a given [center].
 *
 * This function ensures that the given [Offset] is within the bounds of a circle with the specified [radius] around a given [center] and returns the coerced [Offset].
 *
 * @param radius The radius of the circle around the [center].
 * @param center The center of the circle the [Offset] should be within.
 * @return The coerced [Offset].
 */
fun Offset.coerceInRadius(radius: Float, center: Offset): Offset {
    val distance = hypot(this.x - center.x, this.y - center.y)
    if (distance <= radius) return this
    val angle = atan2(this.y - center.y, this.x - center.x)
    return Offset(center.x + radius * cos(angle), center.y + radius * sin(angle))
}

/**
 * A [Composable] function that displays a slider indicator.
 *
 * This function provides a visual indicator for the [vertical value slider][VerticalValueSlider], showing the current blend [position].
 *
 * @param position The position of the indicator.
 * @param color The [Color] of the indicator.
 * @param modifier [Modifier] to be applied to the indicator.
 */
@Composable
fun SliderIndicator(
    position: Offset,
    color: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val indicatorRadius = 10.dp.toPx()
        val clampedY = position.y.coerceIn(indicatorRadius, size.height - indicatorRadius)
        val center = Offset(size.width / 2f, clampedY)

        drawCircle(
            color = Color.White,
            center = center,
            radius = indicatorRadius + 2.dp.toPx()
        )
        drawCircle(
            color = Color.Gray,
            center = center,
            radius = indicatorRadius + 1.dp.toPx(),
            style = Stroke(width = 2.dp.toPx())
        )
        drawCircle(
            color = color,
            center = center,
            radius = indicatorRadius
        )
    }
}

private fun Number.toDegrees(): Double {
    return this.toDouble() / PI * 180
}

private fun Number.toRadians(): Double {
    return this.toDouble() / 180 * PI
}