package dev.traceld.plg.base.config

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.nio.file.Path
import kotlin.io.path.exists

class PlgJsonConfigReader : IPlgConfigReader {
    override fun read(configPath: Path): PlgConfig {
        if (!configPath.exists()) {
            val default = PlgConfig.getDefault()
            val defaultStr = Json.encodeToString(default)
            File(configPath.toUri()).writeText(defaultStr)

            throw RuntimeException("Position logger not configured. Default created at \"$configPath\". Please fill it in.")
        }

        val rawConfigContent = configPath.toFile().readText()
        val parsedContent = Json.decodeFromString<PlgConfig>(rawConfigContent)

        return parsedContent
    }
}