package dev.toast.toastsMCToolBox.lib.rpx

import dev.toast.toastsMCToolBox.lib.rpx.skills.RPSkill
import org.bukkit.entity.Player
import java.util.*

object RPXKit {

    val skills: MutableMap<String, (Player) -> Unit> = mutableMapOf()
    val playerSpecificSkills: MutableMap<UUID, MutableSet<RPSkill>> = mutableMapOf()



}