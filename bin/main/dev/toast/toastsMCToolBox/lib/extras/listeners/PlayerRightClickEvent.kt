package dev.toast.toastsMCToolBox.lib.extras.listeners

import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import java.util.*

class PlayerRightClickEvent(val player: Player, val clickedBlock: Optional<Block>): Event() {


    override fun getHandlers(): HandlerList {
        return handlerList
    }

    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }
}