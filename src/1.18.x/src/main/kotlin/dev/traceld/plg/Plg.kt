package dev.traceld.plg

import dev.traceld.plg.base.IPlgMod
import dev.traceld.plg.base.LocationLogger
import dev.traceld.plg.base.PlayerLocation
import dev.traceld.plg.base.config.PlgConfig
import dev.traceld.plg.base.PlgTicker
import dev.traceld.plg.base.config.PlgJsonConfigReader
import net.minecraft.server.MinecraftServer
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.event.TickEvent
import net.minecraftforge.event.TickEvent.ServerTickEvent
import net.minecraftforge.event.server.ServerStartedEvent
import net.minecraftforge.event.server.ServerStoppedEvent
import net.minecraftforge.event.server.ServerStoppingEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import net.minecraftforge.fml.loading.FMLPaths
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.kotlinforforge.forge.FORGE_BUS
import thedarkcolour.kotlinforforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.forge.runWhenOn
import java.nio.file.Path
import java.time.Instant
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Main mod class. Should be an `object` declaration annotated with `@Mod`.
 * The modid should be declared in this object and should match the modId entry
 * in mods.toml.
 *
 * An example for blocks is in the `blocks` package of this mod.
 */
@Mod(Plg.ID)
object Plg : IPlgMod {
    const val ID = "plg7472616365"

    // the logger for our mod
    val LOGGER: Logger = LogManager.getLogger(ID)

    private lateinit var _minecraftServer: MinecraftServer
    private lateinit var _ticker: PlgTicker
    private lateinit var _config: PlgConfig
    private lateinit var _locationLogger: LocationLogger

    override val configDir: Path
        get() = FMLPaths.CONFIGDIR.get()
    override val configFileName: String
        get() = "plg_config.json"

    init {
        runWhenOn(Dist.DEDICATED_SERVER, toRun = {
            MOD_BUS.addListener(::setup)
            FORGE_BUS.addListener(::onServerSetup)
            FORGE_BUS.addListener(::onShutdown)
            FORGE_BUS.addListener(::onServerTick)
        })
    }

    /**
     * Fired on the global Forge bus.
     */
    private fun setup(ev: FMLCommonSetupEvent) {
        val configReader = PlgJsonConfigReader()

        LOGGER.info("Loading config...")

        _config = configReader.read(configLocation)
        _locationLogger = LocationLogger(_config.logLocation)

        LOGGER.info("Config loaded successfully.")

        LOGGER.info("Preparing logger...")

        _locationLogger.prepareLog()

        LOGGER.info("Prepared logger.")
    }

    private fun onServerSetup(ev: ServerStartedEvent) {
        _minecraftServer = ev.server
        _ticker = PlgTicker(this, _config.interval, _locationLogger)

        LOGGER.info("Ticker has been set up.")
    }

    private fun onServerTick(ev: ServerTickEvent) {
        if (ev.phase == TickEvent.Phase.START) {
            _ticker.onTickStart()
        }
    }

    private fun onShutdown(ev: ServerStoppedEvent) {
        _ticker.dispose()

        LOGGER.info("Disposed ticker.")
    }

    override fun getPlayerLocations(): List<PlayerLocation> {
        val currentTime = Instant.now().epochSecond
        val players = _minecraftServer.playerList.players
        val locations = players.map {
            PlayerLocation(currentTime, it.uuid, it.name.string, it.x, it.y, it.z)
        }

        return locations
    }
}