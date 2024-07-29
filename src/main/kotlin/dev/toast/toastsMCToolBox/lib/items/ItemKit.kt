package dev.toast.toastsMCToolBox.lib.items

import dev.toast.toastsMCToolBox.lib.ToolBox
import dev.toast.toastsMCToolBox.lib.extras.listeners.PlayerLeftClickEvent
import dev.toast.toastsMCToolBox.lib.extras.listeners.PlayerRightClickEvent
import dev.toast.toastsMCToolBox.lib.lore.LoreKit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType


object ItemKit : Listener {
    private val itemHandlers: MutableMap<String, ItemHandler> = mutableMapOf()

    fun registerHandler(handler: ItemHandler) {
        itemHandlers[handler.identifier.toString()] = handler
    }

    fun getHandler(item: ItemStack): ItemHandler? {
        val handlerString = item.itemMeta.persistentDataContainer.get(ToolBox.ITEM_HANDLER_KEY, PersistentDataType.STRING) ?: return null
        return itemHandlers[handlerString]
    }

    fun getHandler(identifier: String): ItemHandler? {
        return itemHandlers[identifier]
    }

    class TItem(private val item: ItemStack) {
        private val meta = item.itemMeta
        private val pdc = meta.persistentDataContainer
        val loreEditor = LoreKit.LoreEditor(item)

        fun setHandler(handler: ItemHandler) {
            pdc.set(ToolBox.ITEM_HANDLER_KEY, PersistentDataType.STRING, handler.identifier.toString())
        }

        fun getHandler(): ItemHandler? {
            val handlerString = pdc.get(ToolBox.ITEM_HANDLER_KEY, PersistentDataType.STRING) ?: return null
            return itemHandlers[handlerString]
        }

        fun applyChanges() {
            loreEditor.applyChanges()
            item.itemMeta = meta
        }

        fun getFinalItem(): ItemStack {
            return item
        }
    }

    @EventHandler
    private fun onItemRightClick(event: PlayerRightClickEvent) {
        val item = event.player.inventory.itemInMainHand
        val meta = item.itemMeta ?: return
        val handler = meta.persistentDataContainer.get(ToolBox.ITEM_HANDLER_KEY, PersistentDataType.STRING)
            ?.let { getHandler(it) } ?: return
        handler.onRightClick(event.player, TItem(item))
    }

    @EventHandler
    private fun onItemLeftClick(event: PlayerLeftClickEvent) {
        val item = event.player.inventory.itemInMainHand
        val meta = item.itemMeta ?: return
        val handler = meta.persistentDataContainer.get(ToolBox.ITEM_HANDLER_KEY, PersistentDataType.STRING)
            ?.let { getHandler(it) } ?: return
        handler.onRightClick(event.player, TItem(item))
    }


    @EventHandler
    private fun onItemShiftLeftClick(event: PlayerLeftClickEvent) {
        val item = event.player.inventory.itemInMainHand
        val meta = item.itemMeta ?: return
        val handler = meta.persistentDataContainer.get(ToolBox.ITEM_HANDLER_KEY, PersistentDataType.STRING)
            ?.let { getHandler(it) } ?: return
        if (event.player.isSneaking) {
            handler.onShiftLeftClick(event.player, TItem(item))
        }
    }

    @EventHandler
    private fun onItemShiftRightClick(event: PlayerRightClickEvent) {
        val item = event.player.inventory.itemInMainHand
        val meta = item.itemMeta ?: return
        val handler = meta.persistentDataContainer.get(ToolBox.ITEM_HANDLER_KEY, PersistentDataType.STRING)
            ?.let { getHandler(it) } ?: return
        if (event.player.isSneaking) {
            handler.onShiftRightClick(event.player, TItem(item))
        }
    }
}