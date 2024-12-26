package com.strabled.composepreferences

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.strabled.composepreferences.utilis.DataStoreManager
import kotlinx.serialization.Serializable
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
    val testTheme = Theme.DARK
    val testString = "testString"
    val testInt = 123
    val testBoolean = true
    val testFloat = 3.14f
    val testLong = 123456789L

    val preferenceMap = mapOf(
        "user" to testUser,
        "theme" to testTheme,
        "string" to testString,
        "int" to testInt,
        "boolean" to testBoolean,
        "float" to testFloat,
        "long" to testLong
    )

    /**
     * Sets up the test environment before each test.
     */
    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        dataStoreManager = DataStoreManager(context)
        dataStoreManager.setPreferences(preferenceMap)
    }

    /**
     * Tests the DataStoreManager's ability to store and retrieve preferences.
     */
    @Test
    fun testDataStoreManager() {
        val userPreference by dataStoreManager.getPreference<User>("user")
        val userPreferenceData = userPreference.value
        assertEquals(userPreferenceData, testUser)

        val themePreference by dataStoreManager.getPreference<Theme>("theme")
        val themePreferenceData: Theme = themePreference.value
        assertEquals(themePreferenceData, testTheme)

        val stringPreference by dataStoreManager.getPreference<String>("string")
        val stringPreferenceData: String = stringPreference.value
        assertEquals(stringPreferenceData, testString)

        val intPreference by dataStoreManager.getPreference<Int>("int")
        val intPreferenceData: Int = intPreference.value
        assertEquals(intPreferenceData, testInt)

        val booleanPreference by dataStoreManager.getPreference<Boolean>("boolean")
        val booleanPreferenceData: Boolean = booleanPreference.value
        assertEquals(booleanPreferenceData, testBoolean)

        val floatPreference by dataStoreManager.getPreference<Float>("float")
        val floatPreferenceData: Float = floatPreference.value
        assertEquals(floatPreferenceData, testFloat)

        val longPreference by dataStoreManager.getPreference<Long>("long")
        val longPreferenceData: Long = longPreference.value
        assertEquals(longPreferenceData, testLong)
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