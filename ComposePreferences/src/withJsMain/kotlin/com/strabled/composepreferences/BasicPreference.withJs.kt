package com.strabled.composepreferences

actual object Logger {
    actual fun e(tag: String, message: String, throwable: Throwable?) {
        if (throwable != null) {
            println("ERROR: [$tag] $message. Throwable: ${throwable.message}")
        } else {
            println("ERROR: [$tag] $message")
        }
    }

    actual fun w(tag: String, message: String, throwable: Throwable?) {
        println("DEBUG: [$tag] $message")
    }

    actual fun i(tag: String, message: String) {
        println("INFO: [$tag] $message")
    }

    actual fun d(tag: String, message: String) {
        println("DEBUG: [$tag] $message")
    }
}