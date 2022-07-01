package dev.traceld.plg.base.config

import java.nio.file.Path

interface IPlgConfigReader {
    fun read(configPath: Path): PlgConfig
}