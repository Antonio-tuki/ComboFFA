package mx.fxmxgragfx.api.tablist.shared.entry

/**
 * Developed by FxMxGRAGFX
 * Project: cCore
 **/

class Tablist {
    private val entries : MutableList<TabEntry> = ArrayList()
    var header = ""
    var footer = ""

    fun getEntry(x : Int, y : Int) : TabEntry {
        return entries.stream()
            .filter { entry: TabEntry -> entry.x == x && entry.y == y }
            .findFirst().orElseGet { TabEntry(x, y, "", -1) }
    }

    fun add(x : Int, y : Int, text : String) {
        entries.add(TabEntry(x, y, text, -1))
    }
}