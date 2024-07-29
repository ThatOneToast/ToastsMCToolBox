package dev.toast.toastsMCToolBox.lib.menus

import dev.toast.toastsMCToolBox.lib.ToolBox
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack

object MenuKit {

    abstract class Menu(open val player: Player? = null) : InventoryHolder, Listener {
        abstract fun getTitle(): Component
        abstract fun getSize(): Int

        abstract fun onRightClickClick(player: Player, item: ItemStack)
        abstract fun onLeftClickClick(player: Player, item: ItemStack)
        abstract fun onMiddleClick(player: Player, item: ItemStack)
        abstract fun onShiftLeftClick(player: Player, item: ItemStack)
        abstract fun onShiftRightClick(player: Player, item: ItemStack)
        abstract fun onWindowBorderLeftClick(player: Player, item: ItemStack)
        abstract fun onWindowBorderRightClick(player: Player, item: ItemStack)
        abstract fun onDoubleClick(player: Player, item: ItemStack)
        abstract fun onNumberKeyClick(player: Player, item: ItemStack)
        abstract fun onDropClick(player: Player, item: ItemStack)
        abstract fun onControlDropClick(player: Player, item: ItemStack)
        abstract fun onCreativeClick(player: Player, item: ItemStack)
        abstract fun onSwapOffhandClick(player: Player, item: ItemStack)
        abstract fun unknownClick(player: Player, item: ItemStack)

        abstract fun onClose(player: Player)
        abstract fun onOpen(player: Player)
        abstract fun filler(): ItemStack?

        private val options: MutableMap<Int, ItemStack> = mutableMapOf()
        private val inventory: Inventory = ToolBox.getPlugin().server.createInventory(player, getSize(), getTitle())

        init {
            register()

            options.forEach { (index, item) ->
                inventory.setItem(index, item)
            }
            (0 until getSize()).forEach { index ->
                if (inventory.getItem(index) == null) {
                    inventory.setItem(index, filler() ?: return@forEach)
                }
            }
        }

        private fun register() {
            ToolBox.getPlugin().server.pluginManager.registerEvents(this, ToolBox.getPlugin())
        }

        override fun getInventory(): Inventory {
            return inventory
        }

        /**
         * Opens the menu for the owner of the inventory.
         * @throws IllegalStateException if the player is null.
         */
        fun open() {
            player?.openInventory(inventory) ?: throw IllegalStateException("Player is null")
        }

        /**
         * Set an item in the menu
         * @param index The index of the item
         * @param item The item to set
         */
        fun setOption(index: Int, item: ItemStack) {
            options[index - 1] = item
            inventory.setItem(index - 1, item)
        }

        /**
         * Remove an item from the menu
         * @param index The index of the item
         */
        fun removeOption(index: Int) {
            options.remove(index - 1)
            inventory.setItem(index - 1, filler() ?: return)
        }


        /**
         * Refreshes the menu for the owner of the inventory.
         */
        fun refresh() {
            player?.closeInventory()
            player?.openInventory(inventory)
        }

        @EventHandler(priority = EventPriority.MONITOR)
        fun onOpenListener(event: InventoryOpenEvent) {
            if (event.inventory == this.inventory) {
                onOpen(event.player as Player)
            }
        }

        @EventHandler(priority = EventPriority.MONITOR)
        fun onCloseListener(event: InventoryOpenEvent) {
            if (event.inventory == this.inventory) {
                onClose(event.player as Player)
            }
        }

        @EventHandler(priority = EventPriority.HIGHEST)
        fun onClickListener(event: InventoryClickEvent) {
            if (event.inventory == this.inventory) {
                val player = event.whoClicked as Player
                val item = event.currentItem ?: return
                when (event.click) {
                    ClickType.LEFT -> onLeftClickClick(player, item)
                    ClickType.RIGHT -> onRightClickClick(player, item)
                    ClickType.SHIFT_LEFT -> onShiftLeftClick(player, item)
                    ClickType.SHIFT_RIGHT -> onShiftRightClick(player, item)
                    ClickType.MIDDLE -> onMiddleClick(player, item)
                    ClickType.WINDOW_BORDER_LEFT -> onWindowBorderLeftClick(player, item)
                    ClickType.WINDOW_BORDER_RIGHT -> onWindowBorderRightClick(player, item)
                    ClickType.DOUBLE_CLICK -> onDoubleClick(player, item)
                    ClickType.NUMBER_KEY -> onNumberKeyClick(player, item)
                    ClickType.DROP -> onDropClick(player, item)
                    ClickType.CONTROL_DROP -> onControlDropClick(player, item)
                    ClickType.CREATIVE -> onCreativeClick(player, item)
                    ClickType.SWAP_OFFHAND -> onSwapOffhandClick(player, item)
                    ClickType.UNKNOWN -> unknownClick(player, item)
                }
                event.isCancelled = true
            }
        }
    }
}
