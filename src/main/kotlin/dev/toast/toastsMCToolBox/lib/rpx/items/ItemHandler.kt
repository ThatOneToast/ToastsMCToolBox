package dev.toast.toastsMCToolBox.lib.rpx.items

import dev.toast.toastsMCToolBox.lib.ToolBox
import dev.toast.toastsMCToolBox.lib.files.FileKit
import dev.toast.toastsMCToolBox.lib.rpx.storage.Store
import org.bukkit.entity.Player
import java.util.*
import kotlin.io.path.absolutePathString

abstract class ItemHandler {


    abstract val name: String
    abstract fun onRightClick(player: Player, item: ItemKit.TItem)
    abstract fun onLeftClick(player: Player, item: ItemKit.TItem)
    abstract fun onShiftLeftClick(player: Player, item: ItemKit.TItem)
    abstract fun onShiftRightClick(player: Player, item: ItemKit.TItem)

    val identifier: UUID
        get() {
            val itemHandlerStorageFile = FileKit.getFile("ItemHandlers.json", ToolBox.getPlugin().dataPath.absolutePathString())
            var itemHandlers = Store.ItemHandlerValues.fromJson(itemHandlerStorageFile.read())
            if (itemHandlers == null) itemHandlers = Store.ItemHandlerValues(mutableMapOf())
            var itemHandlerId = itemHandlers.values[name]
            if (itemHandlerId == null) {
                val id = UUID.randomUUID().toString()
                itemHandlers.values[name] = id
                itemHandlerStorageFile.overwrite(itemHandlers.toJson())
                itemHandlerId =id
            }
            return UUID.fromString(itemHandlerId)

        }

}