package mx.fxmxgragfx.api.scoreboard

import org.bukkit.entity.Player

/**
 * Developed by FxMxGRAGFX
 * Project: cCore
 **/

interface ScoreboardProvider {
    fun title(player : Player) : String
    fun getLines(player: Player): List<String>
}