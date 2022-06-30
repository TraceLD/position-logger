package dev.traceld.plg.base

import java.nio.file.Path

interface IPlgMod {
    val configDir: Path
    val configLocation: Path
        get() = configDir.resolve("plg_config.txt")

    fun getPlayerLocations(): List<PlayerLocation>
}