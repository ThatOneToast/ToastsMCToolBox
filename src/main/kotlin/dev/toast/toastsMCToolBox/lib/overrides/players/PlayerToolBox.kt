package dev.toast.toastsMCToolBox.lib.overrides.players

import dev.toast.toastsMCToolBox.lib.ToolBox
import dev.toast.toastsMCToolBox.lib.files.FileKit
import dev.toast.toastsMCToolBox.lib.overrides.isNegative
import dev.toast.toastsMCToolBox.lib.overrides.toTItem
import dev.toast.toastsMCToolBox.lib.rpx.RPXKit
import dev.toast.toastsMCToolBox.lib.rpx.classes.RPClass
import dev.toast.toastsMCToolBox.lib.rpx.items.ArmorKit
import dev.toast.toastsMCToolBox.lib.rpx.items.ItemKit
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
class PlayerToolBox(val player: Player) {
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
            val race = RPXKit.getRace(player.selectedRace)
            if (value > (race?.getMaxHealth() ?: 20.0)) throw IllegalStateException("Max health cannot be greater than ${race?.getMaxHealth()}")
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue = value
        }

    val maxHealthCap: Double
        get() = RPXKit.getRace(player.selectedRace)?.getMaxHealth() ?: 20.0

    var armor: Double
        get() = player.getAttribute(Attribute.GENERIC_ARMOR)?.baseValue ?: 0.0
        set(value) {
            player.getAttribute(Attribute.GENERIC_ARMOR)?.baseValue = value
        }

    var maxArmor: Double
        get() = player.persistentDataContainer.get(ToolBox.PLAYER_CLASS_MAX_ARMOR, PersistentDataType.DOUBLE) ?: 0.0
        set(value) {
            player.persistentDataContainer.set(ToolBox.PLAYER_CLASS_MAX_ARMOR, PersistentDataType.DOUBLE, value)
        }

    var speed: Double
        get() = player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)?.baseValue ?: 0.0
        set(value) {
            player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)?.baseValue = value
        }

    var maxSpeed: Double
        get() = player.persistentDataContainer.get(ToolBox.PLAYER_CLASS_MAX_SPEED, PersistentDataType.DOUBLE) ?: 0.0
        set(value) {
            player.persistentDataContainer.set(ToolBox.PLAYER_CLASS_MAX_SPEED, PersistentDataType.DOUBLE, value)
        }

    var jumpHeight: Double
        get() = player.getAttribute(Attribute.GENERIC_JUMP_STRENGTH)?.baseValue ?: 0.0
        set(value) {
            player.getAttribute(Attribute.GENERIC_JUMP_STRENGTH)?.baseValue = value
        }

    var maxJumpHeight: Double
        get() = player.persistentDataContainer.get(ToolBox.PLAYER_CLASS_MAX_JUMP_HEIGHT, PersistentDataType.DOUBLE) ?: 0.0
        set(value) {
            player.persistentDataContainer.set(ToolBox.PLAYER_CLASS_MAX_JUMP_HEIGHT, PersistentDataType.DOUBLE, value)
        }

    var attackDamage: Double
        get() = player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.baseValue ?: 0.0
        set(value) {
            player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.baseValue = value
        }

    var maxAttackDamage: Double
        get() = player.persistentDataContainer.get(ToolBox.PLAYER_CLASS_MAX_ATTACK_DAMAGE, PersistentDataType.DOUBLE) ?: 0.0
        set(value) {
            player.persistentDataContainer.set(ToolBox.PLAYER_CLASS_MAX_ATTACK_DAMAGE, PersistentDataType.DOUBLE, value)
        }

    var attackSpeed: Double
        get() = player.getAttribute(Attribute.GENERIC_ATTACK_SPEED)?.baseValue ?: 0.0
        set(value) {
            player.getAttribute(Attribute.GENERIC_ATTACK_SPEED)?.baseValue = value
        }

    var maxAttackSpeed: Double
        get() = player.persistentDataContainer.get(ToolBox.PLAYER_CLASS_MAX_ATTACK_SPEED, PersistentDataType.DOUBLE) ?: 0.0
        set(value) {
            player.persistentDataContainer.set(ToolBox.PLAYER_CLASS_MAX_ATTACK_SPEED, PersistentDataType.DOUBLE, value)
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


    fun giveItem(item: ItemKit.TItem) {
        item.applyChanges()
        player.inventory.addItem(item.getFinalItem())
    }

    fun getArmor(): ArmorKit.EquippedArmor {
        val helm = player.inventory.helmet
        val chest = player.inventory.chestplate
        val legs = player.inventory.leggings
        val boots = player.inventory.boots
        val armor = ArmorKit.EquippedArmor(helm?.toTItem(), chest?.toTItem(), legs?.toTItem(), boots?.toTItem())
        return armor
    }

    fun generateEmptyFiles() {
        val skillsUnitFile = FileKit.getFile("${player.uniqueId}/skills.json", ToolBox.playerStorageUnitsPath)
    }
}

@Suppress("UNUSED")
class SkillsToolBox(val player: Player) {
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
}

@Suppress("UNUSED")
class ManaToolBox(val player: Player) {
    class NotEnoughManaException(message: String) : Exception(message)
    class ManaOverflowException(message: String) : Exception(message)

    var mana: Int
        get() = player.persistentDataContainer.get(ToolBox.PLAYER_CLASS_MANA, PersistentDataType.INTEGER) ?: 0
        set(value) {
            player.persistentDataContainer.set(ToolBox.PLAYER_CLASS_MANA, PersistentDataType.INTEGER, value)
        }

    var maxMana: Int
        get() = player.persistentDataContainer.get(ToolBox.PLAYER_CLASS_MAX_MANA, PersistentDataType.INTEGER) ?: 0
        set(value) {
            player.persistentDataContainer.set(ToolBox.PLAYER_CLASS_MAX_MANA, PersistentDataType.INTEGER, value)
        }

    var manaPerSecond: Int
        get() = player.persistentDataContainer.get(ToolBox.PLAYER_CLASS_MANA_PER_SECOND, PersistentDataType.INTEGER) ?: 0
        set(value) {
            player.persistentDataContainer.set(ToolBox.PLAYER_CLASS_MANA_PER_SECOND, PersistentDataType.INTEGER, value)
        }

    fun spendMana(mana: Int) {
        val newMana = this.mana - mana
        if (newMana.isNegative()) throw NotEnoughManaException("Not enough mana $newMana")
        else this.mana = newMana
    }

    fun giveMana(mana: Int) {
        val newMana = this.mana + mana
        if (newMana > maxMana) throw ManaOverflowException("Mana overflow $newMana")
        else this.mana = newMana
    }


}

val Player.toolbox: PlayerToolBox
    get() = PlayerToolBox(this)

val Player.skillToolBox: SkillsToolBox
    get() = SkillsToolBox(this)

val Player.manaToolBox: ManaToolBox
    get() = ManaToolBox(this)

var Player.selectedRace: String
    get() = this.persistentDataContainer.get(ToolBox.RACE_KEY, PersistentDataType.STRING) ?: ""
    set(value) {
        this.persistentDataContainer.set(ToolBox.RACE_KEY, PersistentDataType.STRING, value)
    }

val Player.raceClass: RPClass?
    get() = RPXKit.getRace(selectedRace)