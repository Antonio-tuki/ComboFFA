package mx.fxmxgragfx.api.tablist.shared

import org.apache.commons.lang.StringUtils
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import mx.fxmxgragfx.api.tablist.shared.entry.Tablist

/**
 * Developed by FxMxGRAGFX
 * Project: cCore
 **/

abstract class TabAdapter {
    fun setupProfiles(player : Player): TabAdapter {
        for (y in 0..19) {
            for (x in 0..3) {
                val index = y * 4 + x
                val text = "§0§$x" + if (y > 9) "§" + y.toString().toCharArray()[0] + "§$" + y.toString()
                    .toCharArray()[1] else "§0§" + y.toString().toCharArray()[0]
                createProfiles(index, text, player)
            }
        }
        return this
    }

    fun handleElement(player : Player, element : Tablist): TabAdapter {
        val rows = getMaxElements(player) / 20
        for (y in 0..19) {
            for (x in 0 until rows) {
                val entry = element.getEntry(x, y)
                val index = y * rows + x
                sendEntryData(player, index, entry.ping, entry.text)
                if (entry.skinData.size > 1) {
                    updateSkin(entry.skinData, index, player)
                }
            }
        }
        return this
    }

    private fun splitText(text : String): Array<String> {
        return if (text.length < 17) {
            arrayOf(text, "")
        } else {
            val left = text.substring(0, 16)
            val right = text.substring(16)
            if (left.endsWith("§")) {
                arrayOf(
                    left.substring(0, left.toCharArray().size - 1),
                    StringUtils.left(ChatColor.getLastColors(left) + "§" + right, 16)
                )
            } else {
                arrayOf(
                    left,
                    StringUtils.left(ChatColor.getLastColors(left) + right, 16)
                )
            }
        }
    }

    fun setupScoreboard(player : Player, text : String, name : String) {
        val splitText = splitText(text)
        val scoreboard =
            if (player.scoreboard == null) Bukkit.getScoreboardManager().newScoreboard else player.scoreboard
        val team = if (scoreboard.getTeam(name) == null) scoreboard.registerNewTeam(name) else scoreboard.getTeam(name)
        if (!team.hasEntry(name)) {
            team.addEntry(name)
        }
        team.prefix = splitText[0]
        team.suffix = splitText[1]
        player.scoreboard = scoreboard
    }

    abstract fun updateSkin(skinData : Array<String>, index : Int, player : Player)
    abstract fun getMaxElements(player : Player) : Int
    abstract fun createProfiles(index : Int, text : String, player : Player)
    abstract fun sendHeaderFooter(player : Player, header : String, footer : String) : TabAdapter
    abstract fun sendEntryData(player : Player, axis : Int, ping : Int, text : String) : TabAdapter
    abstract fun addFakePlayers(player : Player) : TabAdapter
    abstract fun hideRealPlayers(player : Player) : TabAdapter
    abstract fun hidePlayer(player : Player, target : Player) : TabAdapter
    abstract fun showRealPlayers(player : Player) : TabAdapter
    abstract fun showPlayer(player : Player, target : Player) : TabAdapter
}