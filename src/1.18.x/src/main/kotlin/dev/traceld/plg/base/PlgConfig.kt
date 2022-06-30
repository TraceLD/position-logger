package dev.traceld.plg.base

import java.io.File
import kotlin.io.path.exists

class PlgConfig(private val _impl: IPlgMod) {
    private var _logLocation: String? = null

    fun load() {
        val configLoc = _impl.configLocation

        if (!configLoc.exists()) {
            throw RuntimeException("Position logger not configured. Create a file \"$configLoc\".")
        }

        val configContent = File(configLoc.toUri()).readLines()

        if (configContent.isEmpty() || configContent[0].isBlank()) {
            throw RuntimeException("Position logger's config cannot be empty. It must contain a valid path on the first line.")
        }

        _logLocation = configContent[0]
    }

    fun getLogLocation(): String {
        if (_logLocation == null) {
            load()
        }

        return _logLocation!!
    }
}