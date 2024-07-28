package dev.toast.toastsMCToolBox.lib.rpx.skills

import dev.toast.toastsMCToolBox.lib.ToolBox
import dev.toast.toastsMCToolBox.lib.extras.listeners.PlayerLeftClickEvent
import dev.toast.toastsMCToolBox.lib.extras.listeners.PlayerRightClickEvent
import dev.toast.toastsMCToolBox.lib.overrides.message
import dev.toast.toastsMCToolBox.lib.overrides.toolbox
import dev.toast.toastsMCToolBox.lib.rpx.RPXKit
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.*
import java.util.*

@Serializable(with = RPSkillSerializer::class)
abstract class RPSkill: Listener {

    companion object {
        fun deserialize(json: String): RPSkill {
            println(json)
            return Json.decodeFromString(serializer(), json)
        }
    }

    abstract fun getName(): String
    abstract fun getDescription(): String
    abstract fun getLevelRequirement(): Int
    abstract fun getCooldownTime(): Int
    abstract fun getMaxLevel(): Int
    abstract fun getCurrentLevel(): Int
    fun execute(player: Player) = RPXKit.skills[getName()]?.invoke(player)
        ?: throw IllegalStateException("Skill not found")
    abstract val listener: String
    var identifier: String = UUID.randomUUID().toString()

    fun init(): RPSkill {
        ToolBox.getPlugin().server.pluginManager.registerEvents(this, ToolBox.getPlugin())
        if(!RPXKit.skills.contains(getName())) RPXKit.skills[getName()] = this::execute
        return this
    }

    @EventHandler(priority = EventPriority.LOW)
    private fun events(event: Event) {
        val player = when (event) {
            is PlayerRightClickEvent -> event.player
            is PlayerLeftClickEvent -> event.player
            is PlayerInteractEvent -> event.player
            is PlayerRespawnEvent -> event.player
            is PlayerAdvancementDoneEvent -> event.player
            is PlayerJoinEvent -> event.player
            is PlayerQuitEvent -> event.player
            else -> null
        }
        if (player != null && player.toolbox.skills.contains(this)) {
            if (event::class.java.name == listener) {
                execute(player)
                player.message("<green>You have used ${getName()}</green>")
            }
        } else {
            throw IllegalStateException("This listener isn't in place yet. Feel free to make a PR \n" +
                    "https://github.com/ToastArgumentative/ToastsMCToolBox")
        }
    }

    fun isLevelRequirementMet(player: Player): Boolean {
        return player.toolbox.level >= getLevelRequirement()
    }

    fun serialize(): String {
        println(Json.encodeToString(serializer(), this))
        return Json.encodeToString(serializer(), this)
    }

}