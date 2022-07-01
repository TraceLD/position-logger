package dev.traceld.plg.base

import java.io.File

class LocationLogger(private val _fileLoc: String) {
    @Synchronized
    fun writeLocations(csvLocationsLines: List<String>) {
        val csvStr = csvLocationsLines.joinToString()

        File(_fileLoc).appendText(csvStr)
    }

    @Synchronized
    fun prepareLog() {
        val file = File(_fileLoc)

        if (!file.exists()) {
            file.writeText("Timestamp,UUID,Name,X,Y,Z\n")
        }
    }
}