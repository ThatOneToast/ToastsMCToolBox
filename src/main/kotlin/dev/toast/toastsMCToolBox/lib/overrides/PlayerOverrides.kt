package dev.toast.toastsMCToolBox.lib.overrides

import dev.toast.toastsMCToolBox.lib.ToolBox
import dev.toast.toastsMCToolBox.lib.files.FileKit
import dev.toast.toastsMCToolBox.lib.items.ItemKit
import dev.toast.toastsMCToolBox.lib.rpx.RPXKit
import dev.toast.toastsMCToolBox.lib.rpx.classes.RPClass
import dev.toast.toastsMCToolBox.lib.rpx.skills.RPSkill
import dev.toast.toastsMCToolBox.lib.rpx.storage.Store
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

@Suppress("UNUSED")
class ToolBox(val player: Player) {
    var health: Double
        get() = player.health
        set(value) {
            val currentHealth = player.health
            if (currentHealth + value > maxHealth) {
                player.health = maxHealth
            } else player.health = value
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

    val skills: MutableSet<RPSkill.SkillValues>
        get() = RPXKit.playerSpecificSkills[player.uniqueId] ?: mutableSetOf()

    var skillsUnit: Store.SkillsUnit
        get() {
            val uuid = player.uniqueId
            val file = FileKit.getNormyFile("${uuid}/skills.json", ToolBox.playerStorageUnitsPath)
            if (!file.exists()) { file.mkdirs(); file.createNewFile() }
            val contents = file.readText()
            if (contents.isEmpty()) {
                val unit = Store.SkillsUnit(mutableSetOf())
                return unit
            }
            val unit = Store.SkillsUnit.deserialize(contents)
            return unit
        }
        set(value) {
            val uuid = player.uniqueId
            val file = FileKit.getFile("${uuid}/skills.json", ToolBox.playerStorageUnitsPath)
            file.overwrite(value.serialize().trimIndent())
        }

    fun addSkill(skill: RPSkill) {
        var skills = RPXKit.playerSpecificSkills[player.uniqueId]
        if (skills == null) {
            RPXKit.playerSpecificSkills[player.uniqueId] = mutableSetOf()
            skills = RPXKit.playerSpecificSkills[player.uniqueId]
        }

        if (skills!!.contains(skill.serializeToValues())) {
            throw RPSkill.SkillAlreadyExistsException("Skill already exists")
        }
        else RPXKit.playerSpecificSkills[player.uniqueId]!!.add(skill.serializeToValues())
    }

    fun removeSkill(skillName: String) {
        val skills = RPXKit.playerSpecificSkills[player.uniqueId] ?: throw RPSkill.SkillNotFoundException("Skills not found")
        skills.removeIf { it.name == skillName }
    }

    fun hasSkill(skillName: String): Boolean {
        val skills = RPXKit.playerSpecificSkills[player.uniqueId] ?: throw RPSkill.SkillNotFoundException("Skills not found")
        return skills.any { it.name == skillName }
    }

    fun generateEmptyFiles() {
        val skillsUnitFile = FileKit.getFile("${player.uniqueId}/skills.json", ToolBox.playerStorageUnitsPath)
    }

    /**
     * Returns a pair of the skill and the execution function < Skill, Execution >
     * @param skillName The name of the skill
     * @return A pair of the skill and the execution function
     */
    fun getSkill(skillName: String): Pair<RPSkill, (Player) -> Unit>? {
        val skill = RPXKit.getSkillAndExecution(skillName)
        return skill
    }

    fun saveSkills() {
        val playerSkills = RPXKit.playerSpecificSkills[player.uniqueId]
        val unit = Store.SkillsUnit(playerSkills ?: mutableSetOf())
        skillsUnit = unit
    }

    fun giveItem(item: ItemKit.TItem) {
        item.applyChanges()
        player.inventory.addItem(item.getFinalItem())
    }
}

val Player.toolbox: dev.toast.toastsMCToolBox.lib.overrides.ToolBox
    get() = ToolBox(this)

var Player.selectedRace: RPClass?
    get() {
        val selectedClassJsonString = persistentDataContainer.get(ToolBox.RACE_KEY, PersistentDataType.STRING) ?: return null
        return RPClass.fromJson(selectedClassJsonString)
    }
    set(value) {
        persistentDataContainer.set(ToolBox.RACE_KEY, PersistentDataType.STRING, value?.toJson() ?: "")
    }