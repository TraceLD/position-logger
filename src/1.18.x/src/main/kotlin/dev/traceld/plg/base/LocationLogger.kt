package dev.traceld.plg.base

import java.io.File

class LocationLogger(private val _fileLoc: String) {
    @Synchronized
    fun writeLocations(csvLocationsLines: List<String>) {
        val csvStr = csvLocationsLines.joinToString("\n")

        File(_fileLoc).appendText(csvStr)
    }
}