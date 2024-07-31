package dev.toast.toastsMCToolBox.lib.rpx.items

import dev.toast.toastsMCToolBox.lib.ToolBox
import dev.toast.toastsMCToolBox.lib.extras.listeners.PlayerLeftClickEvent
import dev.toast.toastsMCToolBox.lib.extras.listeners.PlayerRightClickEvent
import dev.toast.toastsMCToolBox.lib.lore.LoreKit
import org.bukkit.Material
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

    fun air(): ItemStack {
        return ItemStack(Material.AIR)
    }

    class TItem(private val item: ItemStack) {
        private val meta = item.itemMeta
        val pdc = meta?.persistentDataContainer
        val loreEditor = LoreKit.LoreEditor(item)
        val type = item.type

        private val storage: MutableMap<String, String> = mutableMapOf()

        fun addStorage(key: String, value: String) {
            storage[key] = value
        }

        fun getFromStorage(key: String): String? {
            return storage[key]
        }

        fun setStorage(key: String, value: String) {
            storage[key] = value
        }

        fun getKeysStartingWith(prefix: String): List<String> {
            return storage.keys.filter { it.startsWith(prefix) }
        }

        init {
            if (item.itemMeta != null) {
                val storageString = item.itemMeta.persistentDataContainer.get(ToolBox.ITEM_STORAGE_KEY, PersistentDataType.STRING)
                if (storageString != null) {
                    val storageArray = storageString.split(";")
                    storageArray.forEach {
                        val split = it.split(":")
                        storage[split[0]] = split[1]
                    }
                }
            }

        }

        fun setHandler(handler: ItemHandler) {
            pdc?.set(ToolBox.ITEM_HANDLER_KEY, PersistentDataType.STRING, handler.identifier.toString())
                ?: throw IllegalStateException("Item has no metadata")
        }

        fun getHandler(): ItemHandler? {
            val handlerString = pdc?.get(ToolBox.ITEM_HANDLER_KEY, PersistentDataType.STRING) ?: return null
                ?: throw IllegalStateException("Item has no metadata")
            return itemHandlers[handlerString]
        }

        fun applyChanges() {
            loreEditor.applyChanges()
            item.itemMeta = meta
            val storageString = StringBuilder()
            if (storage.isNotEmpty()) {
                storage.forEach { (key, value) ->
                    storageString.append("$key:$value;")
                }
                storageString.deleteCharAt(storageString.length - 1)
                item.itemMeta.persistentDataContainer.set(ToolBox.ITEM_STORAGE_KEY, PersistentDataType.STRING,storageString.toString())

            }
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
        handler.onLeftClick(event.player, TItem(item))
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