package dev.traceld.plg.base

import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory

class PlgTicker(private val _impl: IPlgMod, private val _interval: Int, private val _locationLogger: LocationLogger) {
    private val _executor = Executors.newSingleThreadExecutor(ThreadFactory { r ->
        val t = Executors.defaultThreadFactory().newThread(r)

        t.isDaemon = true

        return@ThreadFactory t
    })

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
}
