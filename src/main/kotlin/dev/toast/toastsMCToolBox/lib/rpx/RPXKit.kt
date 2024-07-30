package dev.toast.toastsMCToolBox.lib.rpx

import dev.toast.toastsMCToolBox.lib.extras.listeners.PlayerLeftClickEvent
import dev.toast.toastsMCToolBox.lib.extras.listeners.PlayerRightClickEvent
import dev.toast.toastsMCToolBox.lib.overrides.players.skillToolBox
import dev.toast.toastsMCToolBox.lib.overrides.players.toolbox
import dev.toast.toastsMCToolBox.lib.rpx.classes.RPClass
import dev.toast.toastsMCToolBox.lib.rpx.skills.RPSkill
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.*
import java.util.*

object RPXKit : Listener {
    val playerSpecificSkills: MutableMap<UUID, MutableSet<RPSkill.SkillValues>> = mutableMapOf()

    private val races: MutableMap<String, RPClass> = mutableMapOf()

    fun registerRace(race: RPClass) {
        races[race.getName()] = race
    }

    fun getRace(raceName: String): RPClass? {
        return races[raceName]
    }


    private val rightClickListeners: MutableMap<String, Pair<RPSkill, (Player) -> Unit>> = mutableMapOf()
    private val leftClickListeners: MutableMap<String, Pair<RPSkill, (Player) -> Unit>> = mutableMapOf()
    private val interactListeners: MutableMap<String, Pair<RPSkill, (Player) -> Unit>> = mutableMapOf()
    private val respawnListeners: MutableMap<String, Pair<RPSkill, (Player) -> Unit>> = mutableMapOf()
    private val advancementDoneListeners: MutableMap<String, Pair<RPSkill, (Player) -> Unit>> = mutableMapOf()
    private val joinListeners: MutableMap<String, Pair<RPSkill, (Player) -> Unit>> = mutableMapOf()
    private val quitListeners: MutableMap<String, Pair<RPSkill, (Player) -> Unit>> = mutableMapOf()

    fun registerSkill(skill: RPSkill, skillEx: (Player) -> Unit) {
        when (skill.listener) {
            PlayerRightClickEvent::class.java.name -> rightClickListeners[skill.name] = Pair(skill, skillEx)
            PlayerLeftClickEvent::class.java.name -> leftClickListeners[skill.name] = Pair(skill, skillEx)
            PlayerInteractEvent::class.java.name -> interactListeners[skill.name] = Pair(skill, skillEx)
            PlayerRespawnEvent::class.java.name -> respawnListeners[skill.name] = Pair(skill, skillEx)
            PlayerAdvancementDoneEvent::class.java.name -> advancementDoneListeners[skill.name] = Pair(skill, skillEx)
            PlayerJoinEvent::class.java.name -> joinListeners[skill.name] = Pair(skill, skillEx)
            PlayerQuitEvent::class.java.name -> quitListeners[skill.name] = Pair(skill, skillEx)
            else -> throw IllegalStateException("Unknown listener ${skill.listener}" +
                    "Feel free to make a PR \n" +
                    "https://github.com/ToastArgumentative/ToastsMCToolBox"
            )
        }
    }

    fun getSkill(skillName: String): RPSkill? {
        val skill = rightClickListeners[skillName]
            ?: leftClickListeners[skillName]
            ?: interactListeners[skillName]
            ?: respawnListeners[skillName]
            ?: advancementDoneListeners[skillName]
            ?: joinListeners[skillName]
            ?: quitListeners[skillName]
        return skill?.first
    }

    fun isSkillRegistered(skillName: String): Boolean {
        return getSkill(skillName) != null
    }

    fun getSkillExecution(skillName: String): ((Player) -> Unit)? {
        val skill = rightClickListeners[skillName]
            ?: leftClickListeners[skillName]
            ?: interactListeners[skillName]
            ?: respawnListeners[skillName]
            ?: advancementDoneListeners[skillName]
            ?: joinListeners[skillName]
            ?: quitListeners[skillName]

        return skill?.second
    }

    fun getSkillAndExecution(skillName: String): Pair<RPSkill, (Player) -> Unit>? {
        return rightClickListeners[skillName]
            ?: leftClickListeners[skillName]
            ?: interactListeners[skillName]
            ?: respawnListeners[skillName]
            ?: advancementDoneListeners[skillName]
            ?: joinListeners[skillName]
            ?: quitListeners[skillName]
    }



    @EventHandler(priority = EventPriority.LOWEST)
    private fun loadPlayerSkills(event: PlayerJoinEvent) {
        event.player.toolbox.generateEmptyFiles()
        val player = event.player
        val uuid = player.uniqueId
        playerSpecificSkills[uuid] = player.skillToolBox.skillsUnit.skills
        println("Loaded player skills for ${player.name}, skills: ${playerSpecificSkills[uuid]}")

    }

    @EventHandler(priority = EventPriority.LOW)
    private fun playerRightClick(event: PlayerRightClickEvent) {
        for (skill in rightClickListeners) {
            val rpSkill: RPSkill = skill.value.first
            if (event.player.skillToolBox.hasSkill(rpSkill.name)) {
                rpSkill.execute(event.player)
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    private fun playerLeftClick(event: PlayerLeftClickEvent) {
        for (skill in leftClickListeners) {
            val rpSkill: RPSkill = skill.value.first
            if (event.player.skillToolBox.hasSkill(rpSkill.name)) {
                rpSkill.execute(event.player)
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    private fun playerInteract(event: PlayerInteractEvent) {
        for (skill in interactListeners) {
            val rpSkill: RPSkill = skill.value.first
            if (event.player.skillToolBox.hasSkill(rpSkill.name)) {
                rpSkill.execute(event.player)
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    private fun playerRespawn(event: PlayerRespawnEvent) {
        for (skill in respawnListeners) {
            val rpSkill: RPSkill = skill.value.first
            if (event.player.skillToolBox.hasSkill(rpSkill.name)) {
                rpSkill.execute(event.player)
            }
        }
    }
    @EventHandler(priority = EventPriority.LOW)
    private fun playerAdvancementDone(event: PlayerAdvancementDoneEvent) {
        for (skill in advancementDoneListeners) {
            val rpSkill: RPSkill = skill.value.first
            if (event.player.skillToolBox.hasSkill(rpSkill.name)) {
                rpSkill.execute(event.player)
            }
        }
    }
    private fun playerJoin(event: PlayerJoinEvent) {
        for (skill in joinListeners) {
            val rpSkill: RPSkill = skill.value.first
            if (event.player.skillToolBox.hasSkill(rpSkill.name)) {
                rpSkill.execute(event.player)
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    private fun playerQuit(event: PlayerQuitEvent) {
        for (skill in quitListeners) {
            val rpSkill: RPSkill = skill.value.first
            if (event.player.skillToolBox.hasSkill(rpSkill.name)) {
                rpSkill.execute(event.player)
            }
        }
    }

}