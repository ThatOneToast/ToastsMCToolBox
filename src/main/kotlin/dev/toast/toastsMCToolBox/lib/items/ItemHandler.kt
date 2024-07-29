package dev.toast.toastsMCToolBox.lib.items

import org.bukkit.entity.Player
import java.util.*

abstract class ItemHandler {

    abstract fun onRightClick(player: Player, item: ItemKit.TItem)
    abstract fun onLeftClick(player: Player, item: ItemKit.TItem)
    abstract fun onShiftLeftClick(player: Player, item: ItemKit.TItem)
    abstract fun onShiftRightClick(player: Player, item: ItemKit.TItem)

    val identifier = UUID.randomUUID()


}