package com.strabled.composepreferences

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.strabled.composepreferences.utilis.DataStoreManager
import com.strabled.composepreferences.utilis.PreferenceBuilder
import com.strabled.composepreferences.utilis.buildPreferences
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test for the DataStoreManager class.
 */
@RunWith(AndroidJUnit4::class)
class DataStoreManagerTest {

    private lateinit var dataStoreManager: DataStoreManager

    val testUser = User("John", 30)
    val testColor = Color.Red
    val testTheme = Theme.DARK
    val testString = "testString"
    val testInt = 123
    val testBoolean = true
    val testFloat = 3.14f
    val testLong = 123456789L

    val colorKSerializer: KSerializer<Color> = object : KSerializer<Color> {
        override val descriptor: SerialDescriptor
            get() = PrimitiveSerialDescriptor("Color", PrimitiveKind.STRING)

        override fun serialize(encoder: Encoder, value: Color) {
            val string = value.toArgb().toString()
            encoder.encodeString(string)
        }

        override fun deserialize(decoder: Decoder): Color {
            val string = decoder.decodeString()
            return Color(string.toInt())
        }
    }

    val preferences: (DataStoreManager) -> PreferenceBuilder = {
        buildPreferences(it) {
            "user" defaultValue testUser
            "theme" defaultValue testTheme
            "string" defaultValue testString
            "int" defaultValue testInt
            "boolean" defaultValue testBoolean
            "float" defaultValue testFloat
            "long" defaultValue testLong
            "color" defaultValue (testColor serializeWith colorKSerializer)
        }
    }

    /**
     * Sets up the test environment before each test.
     */
    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        dataStoreManager = DataStoreManager(context)
        dataStoreManager.setPreferences(builder = preferences(dataStoreManager))
    }

    /**
     * Tests the DataStoreManager's ability to store and retrieve preferences.
     */
    @Test
    fun testReading() {
        val userPreference = dataStoreManager.getPreference<User>("user")
        val userPreferenceData by userPreference
        assertEquals(testUser, userPreferenceData.value)

        val colorPreference = dataStoreManager.getPreference<Color>("color")
        val colorPreferenceData by colorPreference
        assertEquals(testColor, colorPreferenceData.value)

        val themePreference = dataStoreManager.getPreference<Theme>("theme")
        val themePreferenceData by themePreference
        assertEquals(testTheme, themePreferenceData.value)

        val stringPreference = dataStoreManager.getPreference<String>("string")
        val stringPreferenceData by stringPreference
        assertEquals(testString, stringPreferenceData.value)

        val intPreference = dataStoreManager.getPreference<Int>("int")
        val intPreferenceData by intPreference
        assertEquals(testInt, intPreferenceData.value)

        val booleanPreference = dataStoreManager.getPreference<Boolean>("boolean")
        val booleanPreferenceData by booleanPreference
        assertEquals(testBoolean, booleanPreferenceData.value)

        val floatPreference = dataStoreManager.getPreference<Float>("float")
        val floatPreferenceData by floatPreference
        assertEquals(testFloat, floatPreferenceData.value)

        val longPreference = dataStoreManager.getPreference<Long>("long")
        val longPreferenceData by longPreference
        assertEquals(testLong, longPreferenceData.value)
    }

    /**
     * Tests the DataStoreManager's ability to store and retrieve preferences.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testStoringSerializable() = runTest {
        val preference = dataStoreManager.getPreference<User>("user")
        val testUser2 = User("Jane", 25)
        preference.updateValue(testUser2)
        val preferenceData by preference
        assertEquals(testUser2, preferenceData.value)
    }
}

/**
 * Serializable data class representing a User.
 */
@Serializable
data class User(val name: String, val age: Int)

/**
 * Enum class representing different themes.
 */
enum class Theme {
    LIGHT, DARK
}