package dev.toast.toastsMCToolBox.lib.overrides

import dev.toast.toastsMCToolBox.lib.ToolBox
import dev.toast.toastsMCToolBox.lib.rpx.classes.RPClass
import dev.toast.toastsMCToolBox.lib.rpx.skills.RPSkill
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType

/**
 * Sends a message to the player
 * First converts the message into an adventure component
 * Use Mini message syntax to construct the message
 * @param message The message to send
 */
fun Player.message(message: String) {
    val messagee = MiniMessage.miniMessage().deserialize(message)
    this.sendMessage(messagee)
}

class Stats(val player: Player) {
    var health: Double
        get() = player.health
        set(value) {
            player.health = value
        }

    var maxHealth: Double
        get() = player.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue ?: 20.0
        set(value) {
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue = value
        }

    var armor: Double
        get() = player.getAttribute(Attribute.GENERIC_ARMOR)?.baseValue ?: 0.0
        set(value) {
            player.getAttribute(Attribute.GENERIC_ARMOR)?.baseValue = value
        }


    var speed: Double
        get() = player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)?.baseValue ?: 0.0
        set(value) {
            player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)?.baseValue = value
        }

    var jumpHeight: Double
        get() = player.getAttribute(Attribute.GENERIC_JUMP_STRENGTH)?.baseValue ?: 0.0
        set(value) {
            player.getAttribute(Attribute.GENERIC_JUMP_STRENGTH)?.baseValue = value
        }

    var attackDamage: Double
        get() = player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.baseValue ?: 0.0
        set(value) {
            player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.baseValue = value
        }

    var attackSpeed: Double
        get() = player.getAttribute(Attribute.GENERIC_ATTACK_SPEED)?.baseValue ?: 0.0
        set(value) {
            player.getAttribute(Attribute.GENERIC_ATTACK_SPEED)?.baseValue = value
        }

    var level: Int
        get() = player.persistentDataContainer.get(ToolBox.PLAYER_CLASS_LEVEL, PersistentDataType.INTEGER) ?: 1
        set(value) {
            player.persistentDataContainer.set(ToolBox.PLAYER_CLASS_LEVEL, PersistentDataType.INTEGER, value)
        }

    var maxLevel: Int
        get() = player.persistentDataContainer.get(ToolBox.PLAYER_CLASS_MAX_LEVEL, PersistentDataType.INTEGER) ?: 1
        set(value) {
            player.persistentDataContainer.set(ToolBox.PLAYER_CLASS_MAX_LEVEL, PersistentDataType.INTEGER, value)
        }

    var xp: Int
        get() = player.persistentDataContainer.get(ToolBox.PLAYER_CLASS_EXPERIENCE, PersistentDataType.INTEGER) ?: 0
        set(value) {
            player.persistentDataContainer.set(ToolBox.PLAYER_CLASS_EXPERIENCE, PersistentDataType.INTEGER, value)
        }

    var xpNeeded: Int
        get() = player.persistentDataContainer.get(ToolBox.PLAYER_CLASS_EXPERIENCE_NEEDED, PersistentDataType.INTEGER) ?: 0
        set(value) {
            player.persistentDataContainer.set(ToolBox.PLAYER_CLASS_EXPERIENCE_NEEDED, PersistentDataType.INTEGER, value)
        }

    val canLevelUp: Boolean
        get() = xpNeeded > xp

    val skills: MutableSet<RPSkill>
        get() {
            val byteArrayList: List<ByteArray> = player.persistentDataContainer.get(ToolBox.PLAYER_CLASS_SKILLS, PersistentDataType.LIST.byteArrays()) ?: mutableListOf<ByteArray>()
            return byteArrayList.map { RPSkill.deserialize(it) }.toMutableSet()
        }

    fun addSkill(skill: RPSkill) {
        val byteArrayList: List<ByteArray> = player.persistentDataContainer.get(ToolBox.PLAYER_CLASS_SKILLS, PersistentDataType.LIST.byteArrays()) ?: mutableListOf()
        val skills: MutableList<RPSkill> = byteArrayList.map { RPSkill.deserialize(it) }.toMutableList()
        skills.add(skill)
        val newByteArrayList: List<ByteArray> = skills.map { it.serialize() }.toList()
        player.persistentDataContainer.set(ToolBox.PLAYER_CLASS_SKILLS, PersistentDataType.LIST.byteArrays(), newByteArrayList)
    }

    fun removeSkill(skillName: String) {
        val byteArrayList: List<ByteArray> = player.persistentDataContainer.get(ToolBox.PLAYER_CLASS_SKILLS, PersistentDataType.LIST.byteArrays()) ?: mutableListOf()
        val skills: MutableList<RPSkill> = byteArrayList.map { RPSkill.deserialize(it) }.toMutableList()
        skills.removeIf { it.getName() == skillName }
        val newByteArrayList: List<ByteArray> = skills.map { it.serialize() }.toList()
        player.persistentDataContainer.set(ToolBox.PLAYER_CLASS_SKILLS, PersistentDataType.LIST.byteArrays(), newByteArrayList)
    }
}

val Player.stats: Stats
    get() = Stats(this)

var Player.selectedRace: RPClass?
    get() {
        val selectedClassJsonString = persistentDataContainer.get(ToolBox.RACE_KEY, PersistentDataType.STRING) ?: return null
        return RPClass.fromJson(selectedClassJsonString)
    }
    set(value) {
        persistentDataContainer.set(ToolBox.RACE_KEY, PersistentDataType.STRING, value?.toJson() ?: "")
    }