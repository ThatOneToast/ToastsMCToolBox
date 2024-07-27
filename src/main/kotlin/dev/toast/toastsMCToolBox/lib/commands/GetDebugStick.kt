package dev.toast.toastsMCToolBox.lib.commands

import dev.toast.toastsMCToolBox.lib.PlayerCommand
import dev.toast.toastsMCToolBox.lib.extras.TDebugStick
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class GetDebugStick: PlayerCommand(
    "getdebugstick",
    "toasts.getdebugstick",
    0
) {
    override fun executeCommand(sender: Player, args: Array<String>): Boolean {
        val stick = TDebugStick.getStick()
        if (args.isEmpty()) {
            sender.inventory.addItem(stick)
            return true
        }
        if (args.size == 1) {
            val player = sender.server.getPlayer(args[0])
            player?.inventory?.addItem(stick) ?: sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>Player not found"))
            return true
        }
        return false
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