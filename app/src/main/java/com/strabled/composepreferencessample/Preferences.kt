package com.strabled.composepreferencessample

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.strabled.composepreferences.utilis.PreferenceBuilder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

private val colorKSerializer = object : KSerializer<Color> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Color", PrimitiveKind.INT)

    override fun serialize(encoder: Encoder, value: Color) {
        val string = value.toArgb().toString()
        encoder.encodeString(string)
    }

    override fun deserialize(decoder: Decoder): Color {
        val string = decoder.decodeString()
        return Color(string.toInt())
    }

}

val preferences: PreferenceBuilder.() -> Unit = {
    "sw1" defaultValue false
    "sw2" defaultValue true
    "sw3" defaultValue false
    "sw4" defaultValue true
    "cb1" defaultValue false
    "cb2" defaultValue true
    "cb3" defaultValue true
    "cb4" defaultValue false
    "sl1" defaultValue 0.5f
    "sl2" defaultValue 3
    "sl3" defaultValue 3.6
    "sl4" defaultValue 4L
    "et1" defaultValue "Some text"
    "et2" defaultValue "Some other text"
    "et3" defaultValue ""
    "et4" defaultValue ""
    "et5" defaultValue ""
    "et6" defaultValue ""
    "dl1" defaultValue "item1"
    "dl2" defaultValue "item2"
    "dl3" defaultValue "item3"
    "ddl1" defaultValue "item1"
    "ddl2" defaultValue "item2"
    "ddl3" defaultValue "item3"
    "bl1" defaultValue "item1"
    "bl2" defaultValue "item2"
    "bl3" defaultValue "item3"
    "bl4" defaultValue "item4"
    "msl1" defaultValue setOf("10", "20", "30")
    "msl2" defaultValue setOf("item2", "item3")
    "msl3" defaultValue setOf<String>()
    "cp1" defaultValue (Color.Red serializeWith colorKSerializer)
    "cp2" defaultValue (Color.Green serializeWith colorKSerializer)
    "cp3" defaultValue (Color.Blue serializeWith colorKSerializer)
}
