package com.strabled.composepreferences

import androidx.compose.runtime.Composable
import com.strabled.composepreferences.utilis.DataStoreManager

/**
 * Provides a default instance of [DataStoreManager].
 *
 * This function is a composable that returns a new [DataStoreManager] instance each time it's called.
 * It's intended to be used as a convenient way to get a basic, default-configured [DataStoreManager]
 * for use in UI elements or other parts of the application that require data store access.
 *
 * **Note: ** While this provides a convenient default, for more complex scenarios or if you need
 * to customize the DataStore setup (e.g., specifying a different file name or serializer), you
 * should create and manage your own instance of [DataStoreManager] directly.
 *
 * @return A new instance of [DataStoreManager].
 */
@Composable
actual fun defaultDataStoreManager(): DataStoreManager = DataStoreManager()