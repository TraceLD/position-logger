package dev.traceld.plg.base

import java.nio.file.Path

interface IPlgMod {
    val configDir: Path
    val configFileName: String
    val configLocation: Path
        get() = configDir.resolve(configFileName)

    fun getPlayerLocations(): List<PlayerLocation>
}