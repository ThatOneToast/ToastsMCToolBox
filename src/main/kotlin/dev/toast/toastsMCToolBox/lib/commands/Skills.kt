package dev.toast.toastsMCToolBox.lib.commands

import dev.toast.toastsMCToolBox.lib.PlayerCommand
import dev.toast.toastsMCToolBox.lib.lore.LoreKit
import dev.toast.toastsMCToolBox.lib.menus.SkillsMenu
import dev.toast.toastsMCToolBox.lib.overrides.toolbox
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class Skills : PlayerCommand(
    "skills",
    "toasts.skills",
    5
) {
    override fun executeCommand(sender: Player, args: Array<String>): Boolean {
        val skillValues = sender.toolbox.skillsUnit.skills
        val skillsMenu = if (args.isEmpty()) SkillsMenu(sender) else SkillsMenu(Bukkit.getPlayer(args[0]))

        var skillIndex = 1

        skillValues.forEach {
            val skillItem = ItemStack(Material.EMERALD)
            val displayName = MiniMessage.miniMessage().deserialize("<gold>${it.name}")
            val meta = skillItem.itemMeta
            meta.displayName(displayName)
            skillItem.itemMeta = meta
            val loreKit = LoreKit.LoreEditor(skillItem)
            loreKit.addLore("info1", "<gray>Description: ${it.description}")
            loreKit.addLore("info2", "<gray>Level Requirement: ${it.levelRequirement}")
            loreKit.addLore("info3", "<gray>Cooldown Time: ${it.cooldownTime}")
            loreKit.addLore("info4", "<gray>Max Level: ${it.maxLevel}")
            loreKit.addLore("info5", "<gray>Current Level: ${it.currentLevel}")
            loreKit.applyChanges()
            skillsMenu.setOption(skillIndex, skillItem)
            skillIndex++
        }

        skillsMenu.open()
        return true

    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<String>
    ): List<String> {
        return Bukkit.getOnlinePlayers().map { it.name }.toList()
    }
}