package dev.toast.toastsMCToolBox.lib.extras

import dev.toast.toastsMCToolBox.lib.ToolBox
import dev.toast.toastsMCToolBox.lib.lore.LoreKit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object TDebugStick : Listener {

    fun getStick(): ItemStack {
        val stick = ItemStack(Material.STICK)
        val meta = stick.itemMeta

        meta.displayName(ChatStructure.RAINBOW + "Debug Stick")
        meta.persistentDataContainer.set(NamespacedKey(ToolBox.getPlugin(), "debug"), PersistentDataType.STRING, "Debug Stick")

        stick.itemMeta = meta

        val loreKit = LoreKit.LoreEditor(stick)
        loreKit.addLore("info1", "<red>This debug stick can do things")
        loreKit.addLore("info2", "<red>If you don't kow what, you clearly shouldn't be using this")
        loreKit.addLore("info3", "<red>right?")
        loreKit.addLore("enchantment1", "<green>GOD XXX")
        loreKit.applyChanges()

        return stick
    }


    @EventHandler
    private fun onDebugRightClick(event: PlayerInteractEvent) {
        val item = event.player.inventory.itemInMainHand
        val meta = item.itemMeta ?: return
        val pdc = meta.persistentDataContainer

        if (!pdc.has(NamespacedKey(ToolBox.getPlugin(), "debug"), PersistentDataType.STRING)) return

        if (event.action != Action.RIGHT_CLICK_AIR && event.action != Action.RIGHT_CLICK_BLOCK) return

        if (event.action == Action.RIGHT_CLICK_BLOCK) {


        }


    }
}