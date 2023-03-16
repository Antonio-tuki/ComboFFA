package mx.fxmxgragfx.api.tablist.shared.client

import com.viaversion.viaversion.api.Via
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import protocolsupport.api.ProtocolSupportAPI

/**
 * Developed by FxMxGRAGFX
 * Project: cCore
 **/

object ClientVersionImpl {
    @JvmStatic
    fun getProtocolVersion(player: Player): Int {
        val pluginManager = Bukkit.getPluginManager()
        if (pluginManager.getPlugin("ViaVersion") != null) {
            return Via.getAPI().getPlayerVersion(player)
        } else if (pluginManager.getPlugin("ProtocolSupport") != null) {
            return ProtocolSupportAPI.getProtocolVersion(player).id
        }
        return -1
    }
}