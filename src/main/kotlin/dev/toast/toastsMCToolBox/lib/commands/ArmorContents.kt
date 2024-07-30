package dev.toast.toastsMCToolBox.lib.commands

import dev.toast.toastsMCToolBox.lib.PlayerCommand
import dev.toast.toastsMCToolBox.lib.overrides.players.message
import dev.toast.toastsMCToolBox.lib.overrides.players.toolbox
import dev.toast.toastsMCToolBox.lib.rpx.items.ArmorKit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ArmorContents : PlayerCommand(
    "getarmorcontents",
    "toasts.getarmorcontents",
    7
) {
    override fun executeCommand(sender: Player, args: Array<String>): Boolean {
        if (args.isEmpty()) {
            var mappedArmor = ArmorKit.equippedArmor[sender.uniqueId]
            if (mappedArmor == null) {
                val it = ArmorKit.EquippedArmor(null, null, null, null)
                ArmorKit.equippedArmor[sender.uniqueId] = it
                mappedArmor = it
            }
            val armor = sender.toolbox.getArmor()

            sender.message("<gold>Mapped Armor Contents:</gold> <gray>${mappedArmor}")
            sender.message("<gold>Armor Contents:</gold> <gray>${armor}")
            if (armor.isSameAs(mappedArmor)) {
                sender.message("<green>Armor is the same as the mapped armor")
            } else {
                sender.message("<red>Armor is not the same as the mapped armor")
            }
        } else {
            val player = sender.server.getPlayer(args[0])
            if (player == null) {
                sender.message("<red>Player not found")
                return true
            }


            var mappedArmor = ArmorKit.equippedArmor[player.uniqueId]
            if (mappedArmor == null) {
                val it = ArmorKit.EquippedArmor(null, null, null, null)
                ArmorKit.equippedArmor[player.uniqueId] = it
                mappedArmor = it
            }
            val armor = player.toolbox.getArmor()

            sender.message("<gold>Mapped Armor Contents:</gold> <gray>${mappedArmor}")
            sender.message("<gold>Armor Contents:</gold> <gray>${armor}")
            if (armor.isSameAs(mappedArmor)) {
                sender.message("<green>Armor is the same as the mapped armor")
            } else {
                sender.message("<red>Armor is not the same as the mapped armor")
            }
        }

        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<String>
    ): List<String> {
        val onlinePlayers = sender.server.onlinePlayers
        return onlinePlayers.map { it.name }.toList()
    }
}