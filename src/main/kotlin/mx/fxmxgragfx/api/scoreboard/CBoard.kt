package mx.fxmxgragfx.api.scoreboard

import org.bukkit.entity.Player
import java.util.UUID
import org.bukkit.scoreboard.Scoreboard
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.scoreboard.Objective
import org.bukkit.scoreboard.DisplaySlot
import java.util.ArrayList

/**
 * Developed by FxMxGRAGFX
 * Project: cCore
 **/

class CBoard(player: Player, board : CScoreBoard) {
    private val board : CScoreBoard
    val entries : MutableList<CBoardEntry> = ArrayList()
    val identifiers : MutableList<String> = ArrayList()
    private val uuid : UUID
    val scoreboard : Scoreboard
        get() {
            val player = Bukkit.getPlayer(uuid)
            return if (board.hook || player.scoreboard !== Bukkit.getScoreboardManager().mainScoreboard) {
                player.scoreboard
            } else {
                Bukkit.getScoreboardManager().newScoreboard
            }
        }

    val objective: Objective
        get() {
            val scoreboard = scoreboard
            return if (scoreboard.getObjective("cScoreboard") == null) {
                val objective = scoreboard.registerNewObjective("cScoreboard", "dummy")
                objective.displaySlot = DisplaySlot.SIDEBAR
                objective.displayName = board.provider.title(Bukkit.getPlayer(uuid))
                objective
            } else {
                scoreboard.getObjective("cScoreboard")
            }
        }

    private fun setup(player: Player) {
        val scoreboard = scoreboard
        player.scoreboard = scoreboard
        objective
    }

    fun getEntryAtPosition(pos: Int): CBoardEntry? {
        return if (pos >= entries.size) null else entries[pos]
    }

    fun getUniqueIdentifier(position: Int): String {
        var identifier = getRandomChatColor(position) + ChatColor.WHITE
        while (identifiers.contains(identifier)) {
            identifier = identifier + getRandomChatColor(position) + ChatColor.WHITE
        }
        if (identifier.length > 16) {
            return getUniqueIdentifier(position)
        }
        identifiers.add(identifier)
        return identifier
    }

    private fun getRandomChatColor(position: Int): String {
        return board.chatColorCache[position].toString()
    }

    init {
        uuid = player.uniqueId
        this.board = board
        setup(player)
    }
}