package dev.toast.toastsMCToolBox.lib.extras.listeners

import dev.toast.toastsMCToolBox.lib.overrides.getLoreEditor
import dev.toast.toastsMCToolBox.lib.rpx.items.ArmorKit
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import org.bukkit.inventory.ItemStack


class ArmorEvent(val player: Player, val oldArmor: ArmorKit.EquippedArmor, val newArmor: ArmorKit.EquippedArmor): Event() {

    private val oldHelmet = oldArmor.helmet?.getFinalItem()
    private val newHelmet = newArmor.helmet?.getFinalItem()

    private val oldChestplate = oldArmor.chestplate?.getFinalItem()
    private val newChestplate = newArmor.chestplate?.getFinalItem()

    private val oldLeggings = oldArmor.leggings?.getFinalItem()
    private val newLeggings = newArmor.leggings?.getFinalItem()

    private val oldBoots = oldArmor.boots?.getFinalItem()
    private val newBoots = newArmor.boots?.getFinalItem()


    fun didHelmChange(): Boolean = !compareItem(oldHelmet, newHelmet)
    fun didChestplateChange(): Boolean = !compareItem(oldChestplate, newChestplate)
    fun didLeggingsChange(): Boolean = !compareItem(oldLeggings, newLeggings)
    fun didBootsChange(): Boolean = !compareItem(oldBoots, newBoots)


    override fun getHandlers(): HandlerList {
        return handlerList
    }

    companion object {
        @JvmStatic
        val handlerList = HandlerList()

        /**
         * Return true if the two items are the same
         * This is used to check if the item has changed
         * @param item1 The first item
         * @param item2 The second item
         * @return true if the items are the same
         */
        fun compareItem(item1: ItemStack?, item2: ItemStack?): Boolean {
            if (item1 == null && item2 == null) return true
            if (item1 == null || item2 == null) return false

            val item1Meta = item1.itemMeta
            val item2Meta = item2.itemMeta

            if (item1Meta == null && item2Meta == null) return true
            if (item1Meta == null || item2Meta == null) return false

            val item1LoreKit = item1.getLoreEditor()
            val item2LoreKit = item2.getLoreEditor()

            if (item1LoreKit.getAllLore() != item2LoreKit.getAllLore()) return false

            val itemType1 = item1.type
            val itemType2 = item2.type

            return itemType1 == itemType2
        }

        /**
         * Returns true if the two armor sets are the same
         * This is used to check if the armor has changed
         * @param armorSet1 The first armor set
         * @param armorSet2 The second armor set
         * @return true if the armor sets are the same
         */
        fun compareSets(armorSet1: ArmorKit.EquippedArmor, armorSet2: ArmorKit.EquippedArmor): Boolean {
            val armorSet1Helmet = armorSet1.helmet?.getFinalItem()
            val armorSet2Helmet = armorSet2.helmet?.getFinalItem()

            val armorSet1Chestplate = armorSet1.chestplate?.getFinalItem()
            val armorSet2Chestplate = armorSet2.chestplate?.getFinalItem()

            val armorSet1Leggings = armorSet1.leggings?.getFinalItem()
            val armorSet2Leggings = armorSet2.leggings?.getFinalItem()

            val armorSet1Boots = armorSet1.boots?.getFinalItem()
            val armorSet2Boots = armorSet2.boots?.getFinalItem()

            val sameOrNot = compareItem(armorSet1Helmet, armorSet2Helmet) &&
                    compareItem(armorSet1Chestplate, armorSet2Chestplate) &&
                    compareItem(armorSet1Leggings, armorSet2Leggings) &&
                    compareItem(armorSet1Boots, armorSet2Boots)

            return sameOrNot
        }

    }


}