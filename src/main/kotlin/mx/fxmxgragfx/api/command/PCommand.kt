package mx.fxmxgragfx.api.command

import me.antonio.combo.Combo
import org.bukkit.command.Command
import org.bukkit.command.CommandMap
import java.lang.reflect.Field


/**
 * Developed by FxMxGRAGFX
 * Project: Combo
 **/

class PCommand(inputClass : Class<out Command>) {

    init {
        var field: Field = Combo.INSTANCE.server.javaClass.getDeclaredField("commandMap")
        field.isAccessible = true
        val map : CommandMap = field.get(Combo.INSTANCE.server) as CommandMap
        map.register(Combo.INSTANCE.description.name, inputClass.newInstance())
    }
}