package mx.fxmxgragfx.api.tablist.shared

import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable

/**
 * Developed by FxMxGRAGFX
 * Project: cCore
 **/

class TabRunnable(private val handler : TabHandler) : BukkitRunnable() {
    override fun run() {
        Thread { Bukkit.getServer().onlinePlayers.forEach(handler::sendUpdate) }.run()
    }
}