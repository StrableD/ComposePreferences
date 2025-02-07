package com.strabled.composepreferences

import platform.Foundation.NSLog

actual object Logger {
    actual fun e(tag: String, message: String, throwable: Throwable?) {
        if (throwable != null) {
            NSLog("ERROR: [$tag] $message. Throwable: $throwable CAUSE ${throwable.cause}")
        } else {
            NSLog("ERROR: [$tag] $message")
        }
    }

    actual fun w(tag: String, message: String, throwable: Throwable?) {
        if (throwable != null) {
            NSLog("WARNING: [$tag] $message. Throwable: $throwable CAUSE ${throwable.cause}")
        } else {
            NSLog("WARNING: [$tag] $message")
        }
    }

    actual fun i(tag: String, message: String) {
        NSLog("INFO: [$tag] $message")
    }

    actual fun d(tag: String, message: String) {
        NSLog("DEBUG: [$tag] $message")
    }
}