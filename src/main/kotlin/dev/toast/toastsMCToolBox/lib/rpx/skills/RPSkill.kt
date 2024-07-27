package dev.toast.toastsMCToolBox.lib.rpx.skills

import dev.toast.toastsMCToolBox.lib.overrides.stats
import org.bukkit.entity.Player
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

abstract class RPSkill {

    abstract fun getName(): String
    abstract fun getDescription(): String

    abstract fun getLevelRequirement(): Int

    abstract fun getMaxLevel(): Int
    abstract fun getCurrentLevel(): Int

    abstract fun execute(player: Player)


    fun isLevelRequirementMet(player: Player): Boolean {
        return player.stats.level >= getLevelRequirement()
    }

    fun serialize(): ByteArray {
        val baos = ByteArrayOutputStream()
        val oos = ObjectOutputStream(baos)
        oos.writeObject(this)
        oos.flush()
        return baos.toByteArray()
    }

    companion object {
        fun deserialize(bytes: ByteArray): RPSkill {
            val ois = ObjectInputStream(ByteArrayInputStream(bytes))
            val skill = ois.readObject() as RPSkill
            ois.close()
            return skill
        }






    }



}