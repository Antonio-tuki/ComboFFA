package mx.fxmxgragfx.api.tablist.shared.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import mx.fxmxgragfx.api.tablist.shared.TabHandler

/**
 * Developed by FxMxGRAGFX
 * Project: cCore
 **/

class TablistListener(handler : TabHandler) : Listener {
    private val handler: TabHandler
    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        handler.sendUpdate(event.player)
    }

    init {
        this.handler = handler
    }
}