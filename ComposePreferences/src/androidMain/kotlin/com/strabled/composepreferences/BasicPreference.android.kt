package com.strabled.composepreferences

import android.util.Log

actual object Logger {
    actual fun e(tag: String, message: String, throwable: Throwable?) {
        Log.e(tag, message, throwable)
    }

    actual fun w(tag: String, message: String, throwable: Throwable?) {
        Log.w(tag, message, throwable)
    }

    actual fun i(tag: String, message: String) {
        Log.i(tag, message)
    }

    actual fun d(tag: String, message: String) {
        Log.d(tag, message)
    }
}