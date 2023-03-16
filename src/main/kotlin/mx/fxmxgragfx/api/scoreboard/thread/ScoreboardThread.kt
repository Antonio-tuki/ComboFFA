package mx.fxmxgragfx.api.scoreboard.thread

import org.bukkit.ChatColor
import mx.fxmxgragfx.api.scoreboard.CScoreBoard
import mx.fxmxgragfx.api.scoreboard.CBoardEntry
import java.util.*
import java.util.function.Consumer

/**
 * Developed by FxMxGRAGFX
 * Project: cCore
 **/

class ScoreboardThread(private val cboard : CScoreBoard) : Thread() {
    override fun run() {
        while (true) {
            try {
                tick()
                sleep(cboard.ticks * 50)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun tick() {
        for (player in cboard.plugin.server.onlinePlayers) {
            val board = cboard.boards[player.uniqueId] ?: continue
            val scoreboard = board.scoreboard
            val objective = board.objective
            if (scoreboard == null || objective == null) {
                continue
            }
            val title = ChatColor.translateAlternateColorCodes('&', cboard.provider.title(player))
            if (objective.displayName != title) {
                objective.displayName = title
            }
            var newLines: List<String?> = cboard.provider.getLines(player)
            if (newLines == null || newLines.isEmpty()) {
                board.entries.forEach(Consumer { obj: CBoardEntry -> obj.remove() })
                board.entries.clear()
            } else {
                if (newLines.size > 15) {
                    newLines = newLines.subList(0, 15)
                }
                Collections.reverse(newLines)
                if (board.entries.size > newLines.size) {
                    for (i in newLines.size until board.entries.size) {
                        val entry = board.getEntryAtPosition(i)
                        entry?.remove()
                    }
                }
                var cache = 1
                for (i in newLines.indices) {
                    var entry = board.getEntryAtPosition(i)
                    val line = ChatColor.translateAlternateColorCodes('&', newLines[i])
                    if (entry == null) {
                        entry = CBoardEntry(board, line, i)
                    }
                    entry.text = line
                    entry.setup()
                    entry.send(cache++)
                }
            }
            if (player.scoreboard !== scoreboard && !cboard.hook) {
                player.scoreboard = scoreboard
            }
        }
    }

    init {
        start()
    }
}