package com.strabled.composepreferences

import java.util.logging.Level
import java.util.logging.Logger

actual object Logger {
    private val logger = Logger.getLogger(Logger::class.java.name)

    init {
        logger.level = Level.FINE
    }

    actual fun e(tag: String, message: String, throwable: Throwable?) {
        if (throwable != null) {
            logger.log(Level.SEVERE, message, throwable)
        } else {
            logger.severe(message)
        }
    }

    actual fun w(tag: String, message: String, throwable: Throwable?) {
        if (throwable != null) {
            logger.log(Level.WARNING, message, throwable)
        } else {
            logger.warning(message)
        }
    }

    actual fun i(tag: String, message: String) {
        logger.info(message)
    }

    actual fun d(tag: String, message: String) {
        logger.fine(message)
    }
}