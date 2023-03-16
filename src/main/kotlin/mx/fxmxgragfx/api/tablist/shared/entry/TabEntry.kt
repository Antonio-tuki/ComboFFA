package mx.fxmxgragfx.api.tablist.shared.entry

import mx.fxmxgragfx.api.tablist.shared.skin.SkinType

/**
 * Developed by FxMxGRAGFX
 * Project: cCore
 **/

class TabEntry(val x : Int, val y : Int, val text : String, val ping : Int) {
    var skinData : Array<String> = SkinType.DARK_GRAY.skinData
        private set

}