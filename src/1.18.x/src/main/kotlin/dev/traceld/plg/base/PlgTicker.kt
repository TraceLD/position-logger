package dev.traceld.plg.base

import java.util.concurrent.Executors

class PlgTicker(private val _impl: IPlgMod, _config: PlgConfig) {
    private val _executor = Executors.newSingleThreadExecutor()
    private val _locationLogger = LocationLogger(_config.getLogLocation())

    private var _lastLog: Long = 0

    fun onTickStart() {
        val currentTime = System.currentTimeMillis()

        if (currentTime - _lastLog > 10000) {
            _lastLog = currentTime

            val locations = _impl.getPlayerLocations().map{ it.csvLine }

            _executor.execute {
                _locationLogger.writeLocations(locations)
            }
        }
    }
}
