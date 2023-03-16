package mx.fxmxgragfx.providers

import me.antonio.combo.Combo
import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.entity.Player
import mx.fxmxgragfx.api.CC
import mx.fxmxgragfx.api.tablist.shared.entry.TabProvider
import mx.fxmxgragfx.api.tablist.shared.entry.Tablist

/**
 * Developed by FxMxGRAGFX
 * Project: cCore
 **/

class TablistProvider : TabProvider    {

    override fun getTablist(player: Player): Tablist {
        val element = Tablist()
        element.header = Combo.INSTANCE.config.getString("TABLIST.HEADER").replace("<line>", "\n")
        element.footer = Combo.INSTANCE.config.getString("TABLIST.FOOTER").replace("<line>", "\n")
        val list: List<String> = listOf("LEFT", "MIDDLE", "RIGHT", "FAR-RIGHT")
        for (i in 0..3) {
            for (l in 0..19) {
                element.add(i, l, CC.translate(PlaceholderAPI.setPlaceholders(player, Combo.INSTANCE.config.getString("TABLIST.${list[i]}.${l+1}").replace("<player>", player.displayName))))
            }
        }
        return element
    }
}