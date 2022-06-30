package dev.traceld.plg

import dev.traceld.plg.base.IPlgMod
import dev.traceld.plg.base.PlayerLocation
import dev.traceld.plg.base.PlgConfig
import dev.traceld.plg.base.PlgTicker
import net.minecraft.server.MinecraftServer
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.event.TickEvent
import net.minecraftforge.event.TickEvent.ServerTickEvent
import net.minecraftforge.event.server.ServerStartedEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import net.minecraftforge.fml.loading.FMLPaths
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.kotlinforforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.forge.runWhenOn
import java.nio.file.Path

/**
 * Main mod class. Should be an `object` declaration annotated with `@Mod`.
 * The modid should be declared in this object and should match the modId entry
 * in mods.toml.
 *
 * An example for blocks is in the `blocks` package of this mod.
 */
@Mod(Plg.ID)
object Plg : IPlgMod {
    const val ID = "7472616365_plg"

    // the logger for our mod
    val LOGGER: Logger = LogManager.getLogger(ID)

    private lateinit var _minecraftServer: MinecraftServer
    private lateinit var _ticker: PlgTicker
    private lateinit var _config: PlgConfig

    init {
        runWhenOn(Dist.DEDICATED_SERVER, toRun = {
            MOD_BUS.addListener(this::setup)
            MOD_BUS.addListener(this::onServerSetup)
            MOD_BUS.addListener(this::onServerTick)
        })
    }

    /**
     * Fired on the global Forge bus.
     */
    private fun setup(ev: FMLCommonSetupEvent) {
        _config = PlgConfig(this)

        _config.load()
    }

    private fun onServerSetup(ev: ServerStartedEvent) {
        _minecraftServer = ev.server
        _ticker = PlgTicker(this, _config)
    }

    private fun onServerTick(ev: ServerTickEvent) {
        if (ev.phase == TickEvent.Phase.START) {
            _ticker.onTickStart()
        }
    }

    override val configDir: Path
        get() = FMLPaths.CONFIGDIR.get()

    override fun getPlayerLocations(): List<PlayerLocation> {
        val players = _minecraftServer.playerList.players
        val locations = players.map {
            PlayerLocation(it.uuid, it.name.string, it.x, it.y, it.z)
        }

        return locations
    }
}