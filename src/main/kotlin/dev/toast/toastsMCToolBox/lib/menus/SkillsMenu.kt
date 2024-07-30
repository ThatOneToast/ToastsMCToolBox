package dev.toast.toastsMCToolBox.lib.menus

import dev.toast.toastsMCToolBox.lib.overrides.players.message
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class SkillsMenu(override val player: Player? = null) : MenuKit.Menu(player = player) {
    override fun getTitle(): Component {
        if (player == null) return MiniMessage.miniMessage().deserialize("<red>Skills Menu")
        return MiniMessage.miniMessage().deserialize("<red>Skills Menu - ${player.name}")
    }

    override fun getSize(): Int {
        return 54
    }

    override fun onRightClickClick(player: Player, item: ItemStack) {

    }

    override fun onLeftClickClick(player: Player, item: ItemStack) {

    }

    override fun onMiddleClick(player: Player, item: ItemStack) {

    }

    override fun onShiftLeftClick(player: Player, item: ItemStack) {

    }

    override fun onShiftRightClick(player: Player, item: ItemStack) {

    }

    override fun onWindowBorderLeftClick(player: Player, item: ItemStack) {

    }

    override fun onWindowBorderRightClick(player: Player, item: ItemStack) {

    }

    override fun onDoubleClick(player: Player, item: ItemStack) {

    }

    override fun onNumberKeyClick(player: Player, item: ItemStack) {

    }

    override fun onDropClick(player: Player, item: ItemStack) {

    }

    override fun onControlDropClick(player: Player, item: ItemStack) {

    }

    override fun onCreativeClick(player: Player, item: ItemStack) {

    }

    override fun onSwapOffhandClick(player: Player, item: ItemStack) {

    }

    override fun unknownClick(player: Player, item: ItemStack) {

    }

    override fun onClose(player: Player) {

    }

    override fun onOpen(player: Player) {
        player.message("<green>Opening skills menu")
    }

    override fun filler(): ItemStack? {
        return ItemStack(Material.BLUE_STAINED_GLASS_PANE)
    }
}