package mx.fxmxgragfx.api.scoreboard

import org.bukkit.ChatColor
import org.bukkit.scoreboard.Team

/**
 * Developed by FxMxGRAGFX
 * Project: cCore
 **/

class CBoardEntry(private var board: CBoard, var text: String, position: Int) {
    var team: Team? = null
    private val identifier: String
    fun setup() {
        val scoreboard = board.scoreboard ?: return
        var teamName = identifier
        if (teamName.length > 16) {
            teamName = teamName.substring(0, 16)
        }
        var team = scoreboard.getTeam(teamName)
        if (team == null) {
            team = scoreboard.registerNewTeam(teamName)
        }
        if (!team!!.entries.contains(identifier)) {
            team.addEntry(identifier)
        }
        if (!board.entries.contains(this)) {
            board.entries.add(this)
        }
        this.team = team
    }

    fun send(position: Int) {
        val split = splitTeamText(text)
        team!!.prefix = split[0]
        team!!.suffix = split[1]
        board.objective.getScore(identifier).score = position
    }

    fun remove() {
        board.identifiers.remove(identifier)
        board.scoreboard.resetScores(identifier)
    }

    private fun splitTeamText(input: String): Array<String> {
        val inputLength = input.length
        return if (inputLength > 16) {
            var prefix = input.substring(0, 16)
            val lastColorIndex = prefix.lastIndexOf(ChatColor.COLOR_CHAR)
            var suffix: String
            if (lastColorIndex >= 14) {
                prefix = prefix.substring(0, lastColorIndex)
                suffix = ChatColor.getLastColors(input.substring(0, 17)) + input.substring(lastColorIndex + 2)
            } else {
                suffix = ChatColor.getLastColors(prefix) + input.substring(16)
            }
            if (suffix.length > 16) {
                suffix = suffix.substring(0, 16)
            }
            arrayOf(prefix, suffix)
        } else {
            arrayOf(input, "")
        }
    }

    init {
        identifier = board.getUniqueIdentifier(position)
        setup()
    }
}