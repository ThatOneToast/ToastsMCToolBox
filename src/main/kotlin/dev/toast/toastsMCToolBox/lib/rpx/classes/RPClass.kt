package dev.toast.toastsMCToolBox.lib.rpx.classes

import com.google.gson.Gson
import dev.toast.toastsMCToolBox.lib.overrides.message
import dev.toast.toastsMCToolBox.lib.overrides.selectedRace
import dev.toast.toastsMCToolBox.lib.overrides.toolbox
import org.bukkit.Material
import org.bukkit.entity.Player

abstract class RPClass {

    abstract fun getName(): String
    abstract fun getDescription(): String
    abstract fun getMaterialIcon(): Material

    abstract fun getBaseStartingHealth(): Double
    abstract fun getBaseStartingSpeed(): Double
    abstract fun getBaseStartingJumpHeight(): Double
    abstract fun getBaseStartingAttackDamage(): Double
    abstract fun getBaseStartingDefense(): Double
    abstract fun getBaseStartingArmor(): Double

    abstract fun getMaxHealth(): Double
    abstract fun getMaxSpeed(): Double
    abstract fun getMaxJumpHeight(): Double
    abstract fun getMaxAttackDamage(): Double
    abstract fun getMaxDefense(): Double
    abstract fun getMaxArmor(): Double

    abstract fun getMaxLevel(): Int
    abstract fun experienceToLevel(): Int
    abstract fun experienceMultiplier(): Double

    fun toJson(): String {
        val gson = Gson()
        return gson.toJson(this)
    }
    fun attachToPlayer(player: Player) {
        player.selectedRace = this

        player.toolbox.maxHealth = getBaseStartingHealth()
        player.toolbox.armor = getBaseStartingArmor()
        player.toolbox.speed = getBaseStartingSpeed()
        player.toolbox.jumpHeight = getBaseStartingJumpHeight()
        player.toolbox.attackDamage = getBaseStartingAttackDamage()
        player.toolbox.attackSpeed = getBaseStartingAttackDamage()

        player.toolbox.health = getBaseStartingHealth()

        player.message("<green>You have been assigned the ${getName()} class.")

    }

    companion object {
        fun fromJson(selectedClassJsonString: String): RPClass? {
            val gson = Gson()
            return gson.fromJson(selectedClassJsonString, RPClass::class.java)
        }

    }

}