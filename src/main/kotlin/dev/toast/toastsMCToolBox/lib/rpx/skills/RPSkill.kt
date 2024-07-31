package dev.toast.toastsMCToolBox.lib.rpx.skills

import com.google.gson.Gson
import dev.toast.toastsMCToolBox.lib.callEvent
import dev.toast.toastsMCToolBox.lib.extras.CooldownManager
import dev.toast.toastsMCToolBox.lib.extras.combineUUIDs
import dev.toast.toastsMCToolBox.lib.extras.listeners.PlayerRightClickEvent
import dev.toast.toastsMCToolBox.lib.extras.listeners.SkillExecutedEvent
import dev.toast.toastsMCToolBox.lib.overrides.players.ManaToolBox
import dev.toast.toastsMCToolBox.lib.overrides.players.manaToolBox
import dev.toast.toastsMCToolBox.lib.overrides.players.message
import dev.toast.toastsMCToolBox.lib.overrides.players.toolbox
import dev.toast.toastsMCToolBox.lib.prettify
import dev.toast.toastsMCToolBox.lib.rpx.RPXKit
import org.bukkit.entity.Player
import java.util.*

open class RPSkill(open val name: String) {

    class SkillAlreadyExistsException(message: String) : Exception(message)
    class SkillNotFoundException(message: String) : Exception(message)

    data class SkillValues(
        val name: String,
        val description: String,
        val levelRequirement: Int,
        val cooldownTime: Int,
        val maxLevel: Int,
        val currentLevel: Int,
        val manaCost: Int,
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


    open fun getDescription(): String = "No description"
    open fun getLevelRequirement(): Int = 1
    open fun getCooldownTime(): Int = 0
    open fun getMaxLevel(): Int = 5
    open fun getCurrentLevel(): Int = 1
    open fun getManaCost(): Int = 18

    open fun execute(player: Player) {
        val cooldownUUID = combineUUIDs(player.uniqueId, id)
        var cooldownString = getFromStorage("cooldown")
        if (cooldownString == null) {
            addToStorage("cooldown", cooldownUUID.toString())
            cooldownString = cooldownUUID.toString()
        }
        val cooldown = UUID.fromString(cooldownString)

        val isOnCooldown = CooldownManager.isOnCooldown(cooldown)
        val cooldownTimeSec = CooldownManager.getCooldownTimeInSec(cooldown)

        if (isOnCooldown) {
            player.message("<gold>$name</gold> <gray>is on cooldown for <red>$cooldownTimeSec</red> seconds")
            return
        } else {
            try {
                player.manaToolBox.spendMana(getManaCost())
                RPXKit.getSkillExecution(name)?.invoke(player)
                    ?: throw IllegalStateException("Skill not found")
                CooldownManager.applyCooldown(cooldown, getCooldownTime())
                callEvent(SkillExecutedEvent(this, player))
            } catch (_: ManaToolBox.NotEnoughManaException) {
                player.message("<red>Not enough mana")
            }

        }
    }
    open val listener: String = PlayerRightClickEvent::class.java.name

    private val storage: MutableMap<String, String> = mutableMapOf()
    val id = UUID.randomUUID()



    fun addToStorage(key: String, value: String) {
        storage[key] = value
    }

    fun getFromStorage(key: String): String? {
        return storage[key]
    }

    fun removeFromStorage(key: String) {
        storage.remove(key)
    }

    open fun isLevelRequirementMet(player: Player): Boolean {
        return player.toolbox.level >= getLevelRequirement()
    }

    fun serialize(): String {
        val skillValues = SkillValues(name, getDescription(), getLevelRequirement(), getCooldownTime(), getMaxLevel(), getCurrentLevel(), getManaCost(), listener)
        return skillValues.serialize()
    }

    fun serializeToValues(): SkillValues {
        return SkillValues(
            name,
            getDescription(),
            getLevelRequirement(),
            getCooldownTime(),
            getMaxLevel(),
            getCurrentLevel(),
            getManaCost(),
            listener
        )
    }

}