package dev.toast.toastsMCToolBox.lib.extras.listeners

import dev.toast.toastsMCToolBox.lib.rpx.items.ItemKit
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class SetArmorEquipEvent(val item: ItemKit.TItem, val player: Player, val bonusLevel: Int): Event() {


    override fun getHandlers(): HandlerList {
        return handlerList
    }

    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }
}