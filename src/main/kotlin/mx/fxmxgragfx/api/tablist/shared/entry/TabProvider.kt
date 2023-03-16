package mx.fxmxgragfx.api.tablist.shared.entry

import org.bukkit.entity.Player

/**
 * Developed by FxMxGRAGFX
 * Project: cCore
 **/

interface TabProvider {
    fun getTablist(player: Player) : Tablist
}