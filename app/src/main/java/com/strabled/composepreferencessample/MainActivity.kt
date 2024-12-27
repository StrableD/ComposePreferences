package com.strabled.composepreferencessample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.strabled.composepreferences.LocalDataStoreManager
import com.strabled.composepreferences.ProvideDataStoreManager
import com.strabled.composepreferences.setPreferences
import com.strabled.composepreferencessample.ui.theme.ComposePreferencesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProvideDataStoreManager {
                setPreferences(preferences)
                ComposePreferencesTheme {
                    SettingsScreen()
                }
            }
        }
    }
}
