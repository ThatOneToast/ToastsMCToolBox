package dev.toast.toastsMCToolBox.lib.extras.listeners

import dev.toast.toastsMCToolBox.lib.rpx.skills.RPSkill
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class SkillExecutedEvent(val skill: RPSkill, val player: Player): Event() {


    override fun getHandlers(): HandlerList {
        return handlerList
    }

    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }
}