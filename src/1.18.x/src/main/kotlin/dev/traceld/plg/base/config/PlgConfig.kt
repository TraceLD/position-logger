package dev.traceld.plg.base.config

@kotlinx.serialization.Serializable
data class PlgConfig(val logLocation: String, val interval: Int) {
    companion object {
        fun getDefault(): PlgConfig = PlgConfig("pathToLocationLog", 10000)
    }
}