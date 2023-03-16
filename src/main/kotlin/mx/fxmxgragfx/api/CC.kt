package mx.fxmxgragfx.api

import org.bukkit.ChatColor
import kotlin.streams.toList

/**
 * Developed by FxMxGRAGFX
 * Project: cCore
 **/

object CC {

    @JvmStatic
    fun translate(input : String) : String {
        return ChatColor.translateAlternateColorCodes('&', input)
    }

    @JvmStatic
    fun translate(input : List<String>) : List<String> {
        return input.stream().map(CC::translate).toList()
    }
}