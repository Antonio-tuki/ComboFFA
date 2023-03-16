package mx.fxmxgragfx.api.scoreboard

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

/**
 * Developed by FxMxGRAGFX
 * Project: cCore
 **/

class CBoardListener(private val board : CScoreBoard) : Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        board.boards[event.player.uniqueId] = CBoard(event.player, board)
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        board.boards.remove(event.player.uniqueId)
        event.player.scoreboard = Bukkit.getScoreboardManager().mainScoreboard
    }
}