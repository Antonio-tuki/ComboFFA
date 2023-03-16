package mx.fxmxgragfx.api.handler

import me.antonio.combo.Combo
import org.bukkit.Bukkit
import org.bukkit.event.Listener


/**
 * Developed by FxMxGRAGFX
 * Project: Combo
 **/

class Handler(inputClass : Class<out Listener>) {

    init {
        Bukkit.getServer().pluginManager.registerEvents(inputClass.newInstance(), Combo.INSTANCE)
    }
}