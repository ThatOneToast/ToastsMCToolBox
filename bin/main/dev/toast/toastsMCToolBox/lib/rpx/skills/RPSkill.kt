package dev.toast.toastsMCToolBox.lib.rpx.skills

import com.google.gson.Gson
import dev.toast.toastsMCToolBox.lib.ToolBox
import dev.toast.toastsMCToolBox.lib.extras.listeners.PlayerLeftClickEvent
import dev.toast.toastsMCToolBox.lib.extras.listeners.PlayerRightClickEvent
import dev.toast.toastsMCToolBox.lib.overrides.message
import dev.toast.toastsMCToolBox.lib.overrides.toolbox
import dev.toast.toastsMCToolBox.lib.prettify
import dev.toast.toastsMCToolBox.lib.rpx.RPXKit
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.*

abstract class RPSkill: Listener {

    data class SkillValues(
        val name: String,
        val description: String,
        val levelRequirement: Int,
        val cooldownTime: Int,
        val maxLevel: Int,
        val currentLevel: Int,
        val listener: String
    ) {
        fun serialize(): String {
            val json = Gson().toJson(this)
            println("SkillsValue Serialize Function - ${json.prettify()}")
            return json
        }
        companion object {
            fun deserialize(json: String): SkillValues? {
                val jsonObject = Gson().fromJson(json, SkillValues::class.java)
                return jsonObject
            }
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

    private val storage: MutableMap<String, String> = mutableMapOf()

    fun init() {
        ToolBox.getPlugin().server.pluginManager.registerEvents(this, ToolBox.getPlugin())
        if(!RPXKit.skills.contains(getName())) RPXKit.skills[getName()] = this::execute
        println("Initialized skill ${getName()}")
    }

    fun addToStorage(key: String, value: String) {
        storage[key] = value
    }

    fun getFromStorage(key: String): String? {
        return storage[key]
    }

    fun removeFromStorage(key: String) {
        storage.remove(key)
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
        val skillValues = SkillValues(getName(), getDescription(), getLevelRequirement(), getCooldownTime(), getMaxLevel(), getCurrentLevel(), listener)
        return skillValues.serialize()
    }

    fun serializeToValues(): SkillValues {
        return SkillValues(
            getName(),
            getDescription(),
            getLevelRequirement(),
            getCooldownTime(),
            getMaxLevel(),
            getCurrentLevel(),
            listener
        )
    }

}