package dev.traceld.plg.base

import java.util.*

data class PlayerLocation(
    val timestamp: Long,
    val uuid: UUID,
    val name: String,
    val x: Double,
    val y: Double,
    val z: Double
){
    val csvLine
        get() = "$timestamp, $uuid, $name, $x, $y, $z\n"
}
