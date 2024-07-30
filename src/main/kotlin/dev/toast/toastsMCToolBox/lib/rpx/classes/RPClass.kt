package dev.toast.toastsMCToolBox.lib.rpx.classes

import com.google.gson.Gson
import dev.toast.toastsMCToolBox.lib.overrides.players.manaToolBox
import dev.toast.toastsMCToolBox.lib.overrides.players.message
import dev.toast.toastsMCToolBox.lib.overrides.players.selectedRace
import dev.toast.toastsMCToolBox.lib.overrides.players.toolbox
import org.bukkit.Material
import org.bukkit.entity.Player

open class RPClass {

    open fun getName(): String = "Unidentified"
    open fun getDescription(): String = "No description"
    open fun getMaterialIcon(): Material = Material.STONE

    open fun getBaseStartingHealth(): Double = 40.0
    open fun getBaseStartingSpeed(): Double = 0.2
    open fun getBaseStartingJumpHeight(): Double = 1.2
    open fun getBaseStartingAttackDamage(): Double = 3.0
    open fun getBaseStartingAttackSpeed(): Double = 0.2
    open fun getBaseStartingDefense(): Double = 0.0
    open fun getBaseStartingArmor(): Double = 0.0

    open fun getMaxHealth(): Double = 40.0
    open fun getMaxSpeed(): Double = 0.2
    open fun getMaxJumpHeight(): Double = 1.2
    open fun getMaxAttackDamage(): Double = 3.0
    open fun getMaxAttackSpeed(): Double = 0.2
    open fun getMaxDefense(): Double = 0.0
    open fun getMaxArmor(): Double = 0.0

    open fun getMaxMana(): Int = 100
    open fun getMaxManaPerSecond(): Int = 10

    open fun getMaxLevel(): Int = 1
    open fun experienceToLevel(): Int = 1
    open fun experienceMultiplier(): Double = 1.0

    fun toJson(): String {
        val gson = Gson()
        return gson.toJson(this)
    }

    fun attachToPlayer(player: Player) {
        player.selectedRace = this.getName()

        player.toolbox.health = getBaseStartingHealth()
        player.toolbox.maxHealth = getBaseStartingHealth()

        player.toolbox.maxArmor = getMaxArmor()
        player.toolbox.armor = getBaseStartingArmor()

        player.toolbox.maxSpeed = getMaxSpeed()
        player.toolbox.speed = getBaseStartingSpeed()

        player.toolbox.maxJumpHeight = getMaxJumpHeight()
        player.toolbox.jumpHeight = getBaseStartingJumpHeight()

        player.toolbox.maxAttackDamage = getMaxAttackDamage()
        player.toolbox.attackDamage = getBaseStartingAttackDamage()

        player.toolbox.maxAttackSpeed = getMaxAttackSpeed()
        player.toolbox.attackSpeed = getBaseStartingAttackSpeed()


        player.toolbox.health = getBaseStartingHealth()

        player.manaToolBox.maxMana = getMaxMana()
        player.manaToolBox.manaPerSecond = getMaxManaPerSecond()
        player.manaToolBox.mana = getMaxMana()


        player.message("<green>You have been assigned the ${getName()} class.")

    }

    companion object {
        fun fromJson(selectedClassJsonString: String): RPClass? {
            val gson = Gson()
            return gson.fromJson(selectedClassJsonString, RPClass::class.java)
        }



    }

}