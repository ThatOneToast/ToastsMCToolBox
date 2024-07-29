package dev.toast.toastsMCToolBox.lib.extras.listeners

import dev.toast.toastsMCToolBox.lib.ToolBox
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import java.util.*

object Caller : Listener {

    init {
        ToolBox.getPlugin().server.pluginManager.registerEvents(this, ToolBox.getPlugin())
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun interact(event: PlayerInteractEvent) {
        if (event.action == Action.RIGHT_CLICK_BLOCK || event.action == Action.RIGHT_CLICK_AIR) {
            ToolBox.getPlugin().server.pluginManager.callEvent(
                PlayerRightClickEvent(
                    event.player,
                    Optional.ofNullable(event.clickedBlock)
                )
            )
        } else if (event.action == Action.LEFT_CLICK_BLOCK || event.action == Action.LEFT_CLICK_AIR) {
            ToolBox.getPlugin().server.pluginManager.callEvent(
                PlayerLeftClickEvent(
                    event.player,
                    Optional.ofNullable(event.clickedBlock)
                )
            )
        }
    }


}