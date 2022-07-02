package dev.traceld.plg.base

import java.util.concurrent.Executors

class PlgTicker(private val _impl: IPlgMod, private val _interval: Int, private val _locationLogger: LocationLogger) {
    private val _executor = Executors.newSingleThreadExecutor()

    private var _lastLog: Long = 0

    fun onTickStart() {
        val currentTime = System.currentTimeMillis()

        if (currentTime - _lastLog > _interval) {
            _lastLog = currentTime

            val locations = _impl.getPlayerLocations().map{ it.csvLine }

            _executor.execute {
                _locationLogger.writeLocations(locations)
            }
        }
    }

    fun dispose() {
        _executor.shutdown()
    }
}
