package mx.fxmxgragfx.api.scoreboard

import org.bukkit.ChatColor
import org.bukkit.plugin.java.JavaPlugin
import mx.fxmxgragfx.api.scoreboard.thread.ScoreboardThread
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * Developed by FxMxGRAGFX
 * Project: cCore
 **/

class CScoreBoard(val plugin : JavaPlugin, val provider : ScoreboardProvider) {
    var thread : ScoreboardThread? = null
    var listeners :  CBoardListener? = null
    val boards : MutableMap<UUID, CBoard> = ConcurrentHashMap()
    val ticks : Long = 2
    val hook = false
    val chatColorCache = ChatColor.values()

    private fun setup() {
        listeners = CBoardListener(this)
        plugin.server.pluginManager.registerEvents(listeners, plugin)
        if (thread != null) {
            thread!!.stop()
            thread = null
        }
        plugin.server.onlinePlayers.forEach { player -> boards.putIfAbsent(player.uniqueId, CBoard(player, this)) }
        thread = ScoreboardThread(this)
    }

    init {
        setup()
    }
}