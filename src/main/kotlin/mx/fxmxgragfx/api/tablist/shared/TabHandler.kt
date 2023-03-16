package mx.fxmxgragfx.api.tablist.shared

import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import mx.fxmxgragfx.api.tablist.shared.entry.TabProvider

/**
 * Developed by FxMxGRAGFX
 * Project: cCore
 **/

class TabHandler(private val adapter : TabAdapter, private val handler : TabProvider, plugin : JavaPlugin, ticks : Long) {
    fun sendUpdate(player : Player) {
        val tabElement = handler.getTablist(player)
        adapter
            .setupProfiles(player)
            .showRealPlayers(player).addFakePlayers(player)
            .hideRealPlayers(player).handleElement(player, tabElement)
            .sendHeaderFooter(player, tabElement.header, tabElement.footer)
    }

    init {
        TabRunnable(this).runTaskTimer(plugin, 20L, ticks)

        // register listener for hiding players from tab
        //Bukkit.getPluginManager().registerEvents(new PlayerListener(this), plugin);
    }
}