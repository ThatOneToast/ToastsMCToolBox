package dev.toast.toastsMCToolBox.lib.commands

import dev.toast.toastsMCToolBox.lib.PlayerCommand
import dev.toast.toastsMCToolBox.lib.lore.LoreKit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SetLoreOf: PlayerCommand(
    name = "setlore",
    permission = "toasts.setlore",
    cooldown = 0
) {
    override fun executeCommand(sender: Player, args: Array<String>): Boolean {
        val identifier = args[0]
        val loreString = args.drop(1).joinToString(" ")
        val item = sender.inventory.itemInMainHand

        val loreEditor = LoreKit.LoreEditor(item)
        loreEditor.setLore(identifier, loreString)
        loreEditor.applyChanges()

        return true

    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<String>
    ): List<String> {
        if (args.size == 1) {
            if (sender is Player) {
                val mainHand = sender.inventory.itemInMainHand
                val loreEditor = LoreKit.LoreEditor(mainHand)
                return loreEditor.getLoreKeys()
            }
        }
        return emptyList()
    }
}