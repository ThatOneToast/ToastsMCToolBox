package dev.toast.toastsMCToolBox.lib.rpx.items

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent
import dev.toast.toastsMCToolBox.lib.callEvent
import dev.toast.toastsMCToolBox.lib.extras.listeners.ArmorEvent
import dev.toast.toastsMCToolBox.lib.extras.listeners.ArmorEvent.Companion.compareItem
import dev.toast.toastsMCToolBox.lib.overrides.players.toolbox
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import java.util.*

object ArmorKit : Listener {

    data class EquippedArmor(
        var helmet: ItemKit.TItem?,
        var chestplate: ItemKit.TItem?,
        var leggings: ItemKit.TItem?,
        var boots: ItemKit.TItem?
    ) {
        fun toSet(): Set<ItemKit.TItem?> {
            return setOf(helmet, chestplate, leggings, boots)
        }

        fun isSameAs(armor: EquippedArmor): Boolean {
            return isTheSame(this, armor)
        }

        fun isNotSameAs(armor: EquippedArmor): Boolean {
            return !isTheSame(this, armor)
        }

        override fun toString(): String {
            val helmMaterial = helmet?.getFinalItem()?.type ?: "null"
            val chestMaterial = chestplate?.getFinalItem()?.type ?: "null"
            val legMaterial = leggings?.getFinalItem()?.type ?: "null"
            val bootMaterial = boots?.getFinalItem()?.type ?: "null"
            return "Helmet: $helmMaterial, Chestplate: $chestMaterial, Leggings: $legMaterial, Boots: $bootMaterial"
        }

        /**
         * Returns the differences between two sets of armor.
         *
         * if the differences is null, then there are no differences.
         * if the differences is empty, then there are no differences.
         * if the differences is not empty, then there are differences.
         * Example usage:
         *
         * val differences = oldArmor.getDifference(newArmor)
         * differences?.forEach { (slot, items) ->
         *     val (oldItem, newItem) = items
         *     println("$slot changed from ${oldItem?.type} to ${newItem?.type}")
         * } ?: println("No differences found.")
         *
         * The differences are returned as a map of slot names to pairs of old and new items.
         * @param other The other set of armor to compare to.
         * @return A map of slot names to pairs of old and new items.
         *
         */
        fun getDifference(other: EquippedArmor): Map<String, Pair<ItemKit.TItem?, ItemKit.TItem?>>? {
            val differences = mutableMapOf<String, Pair<ItemKit.TItem?, ItemKit.TItem?>>()

            if (this.helmet != other.helmet) {
                val oldItem = this.helmet
                val newItem = other.helmet
                val compared = compareItem(oldItem?.getFinalItem(), newItem?.getFinalItem())
                if (!compared) differences["helmet"] = Pair(oldItem, newItem)
            }
            if (this.chestplate != other.chestplate) {
                val oldItem = this.chestplate
                val newItem = other.chestplate
                val compared = compareItem(oldItem?.getFinalItem(), newItem?.getFinalItem())
                if (!compared) differences["chestplate"] = Pair(oldItem, newItem)
            }
            if (this.leggings != other.leggings) {
                val oldItem = this.leggings
                val newItem = other.leggings
                val compared = compareItem(oldItem?.getFinalItem(), newItem?.getFinalItem())
                if (!compared) differences["leggings"] = Pair(oldItem, newItem)
            }
            if (this.boots != other.boots) {
                val oldItem = this.boots
                val newItem = other.boots
                val compared = compareItem(oldItem?.getFinalItem(), newItem?.getFinalItem())
                if (!compared) differences["boots"] = Pair(oldItem, newItem)
            }

            return differences.ifEmpty { null }
        }

        companion object {
            fun isTheSame(armor1: EquippedArmor, armor2: EquippedArmor): Boolean {
                armor1.getDifference(armor2) ?: return true
                return false
            }
        }
    }

    val equippedArmor: MutableMap<UUID, EquippedArmor> = mutableMapOf()

    @EventHandler
    private fun playerJoin(event: PlayerJoinEvent) {
        val player = event.player
        val uuid = player.uniqueId
        equippedArmor[uuid] = player.toolbox.getArmor()
    }


    @EventHandler
    private fun armorTagger(event: PlayerArmorChangeEvent) {
        val player = event.player
        val oldArmor: EquippedArmor = equippedArmor[player.uniqueId]!!
        val newArmor: EquippedArmor = player.toolbox.getArmor()

        if (newArmor.isNotSameAs(oldArmor)) {
            equippedArmor[player.uniqueId] = newArmor
            callEvent(ArmorEvent(player, oldArmor, newArmor))
        }


    }





}