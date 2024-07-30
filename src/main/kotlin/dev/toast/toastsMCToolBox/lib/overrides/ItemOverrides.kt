package dev.toast.toastsMCToolBox.lib.overrides

import dev.toast.toastsMCToolBox.lib.lore.LoreKit
import dev.toast.toastsMCToolBox.lib.rpx.items.ItemKit
import org.bukkit.inventory.ItemStack


fun ItemStack.toTItem(): ItemKit.TItem {
    if (itemMeta == null) throw IllegalStateException("Item has no metadata - Conversion to TItem failed")
    return ItemKit.TItem(this)
}

fun ItemStack.getLoreEditor(): LoreKit.LoreEditor {
    return LoreKit.LoreEditor(this)
}
