package dev.toast.toastsMCToolBox.lib.rpx

import dev.toast.toastsMCToolBox.lib.ToolBox
import dev.toast.toastsMCToolBox.lib.rpx.skills.RPSkill
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import java.util.*

object RPXKit : Listener {

    init {
        init()
    }

    private fun init() {
        ToolBox.getPlugin().server.pluginManager.registerEvents(this, ToolBox.getPlugin())
    }

    val skills: MutableMap<String, (Player) -> Unit> = mutableMapOf()
    val playerSpecificSkills: MutableMap<UUID, MutableSet<RPSkill>> = mutableMapOf()


    @EventHandler(priority = EventPriority.LOWEST)
    private fun loadPlayerSkills(event: PlayerJoinEvent) {
        val player = event.player
        val uuid = player.uniqueId
        playerSpecificSkills[uuid] = mutableSetOf()


    }
}