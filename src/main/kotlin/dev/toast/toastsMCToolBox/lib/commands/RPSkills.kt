package dev.toast.toastsMCToolBox.lib.commands

import dev.toast.toastsMCToolBox.lib.PlayerCommand
import dev.toast.toastsMCToolBox.lib.overrides.players.message
import dev.toast.toastsMCToolBox.lib.overrides.players.skillToolBox
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class RPSkills : PlayerCommand(
    "rpskills",
    "toasts.rpskills",
    0
) {
    override fun executeCommand(sender: Player, args: Array<String>): Boolean {
        val skills = sender.skillToolBox.skills
        val skillNames = skills.map { it.name }.toList()
        sender.message("<green>Skills:</green> <gold>${skillNames.joinToString(", ")}")
        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<String>
    ): List<String> {
        return emptyList()
    }
}