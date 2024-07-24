package dev.toast.toastsMCToolBox.lib.lore

import com.google.gson.Gson
import dev.toast.toastsMCToolBox.lib.ToolBox
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

class LoreKit {


    data class LoreStorage(val lore: MutableList<String> = mutableListOf()) {
        fun toByteArray(): ByteArray {
            return Gson().toJson(this).toByteArray()
        }

        companion object {
            fun fromByteArray(byteArray: ByteArray): LoreStorage {
                return Gson().fromJson(String(byteArray), LoreStorage::class.java)
            }
        }
    }

    class LoreEditor(private val item: ItemStack) {
        private val itemMeta = item.itemMeta
        private val pdc = itemMeta.persistentDataContainer
        private val taggedLore = pdc.get(ToolBox.LORE_KEY, PersistentDataType.BYTE_ARRAY)
        private val lore: MutableMap<String, String> = mutableMapOf()

        init {
            if (taggedLore == null) pdc.set(ToolBox.LORE_KEY, PersistentDataType.BYTE_ARRAY, LoreStorage().toByteArray())
            parsePDC()

        }

        private fun parsePDC() {
            if (taggedLore != null) {
                val loreStorage = LoreStorage.fromByteArray(taggedLore)
                loreStorage.lore.forEach {
                    val split = it.split(":")
                    lore[split[0]] = split[1]
                }
            } else {
                lore.clear()
            }
        }

        /**
         * @param identifier The identifier of the lore to set
         *@param lore Can be in mini message format, all lore will be parsed by mini message.
         *
         */
        fun setLore(identifier: String, lore: String?) {
            if (lore == null) {
                removeLore(identifier)
            } else {
                this.lore[identifier] = lore
            }
        }

        /**
         * @param identifier The identifier of the lore to get
         * @param lore Can be in mini message format, all lore will be parsed by mini message.
         * @throws IllegalStateException if the lore identifier already exists
         */
        fun addLore(identifier: String, lore: String) {
            if (this.lore.containsKey(identifier)) {
                throw IllegalStateException("Lore with identifier $identifier already exists")
            } else {
                this.lore[identifier] = lore
            }
        }

        /**
         * @param identifier The identifier of the lore to remove
         * @throws IllegalStateException if the lore identifier does not exist
         */
        fun removeLore(identifier: String) {
            if (!this.lore.containsKey(identifier)) {
                throw IllegalStateException("Lore with identifier $identifier does not exist")
            } else {
                this.lore.remove(identifier)
            }

        }

        /**
         * @param identifier The identifier of the lore to get
         * @return The lore
         * @throws IllegalStateException if the lore identifier does not exist
         */
        fun getLore(identifier: String): String {
            if (!this.lore.containsKey(identifier)) {
                throw IllegalStateException("Lore with identifier $identifier does not exist")
            } else {
                return this.lore[identifier]!!
            }
        }

        fun getAllLore(): Map<String, String> {
            return this.lore
        }

        fun replaceAllLore(lore: Map<String, String>) {
            this.lore.clear()
            this.lore.putAll(lore)
        }

        fun getLoreKeys(): List<String> {
            return this.lore.keys.toList()
        }

        fun applyChanges() {
            val loreList: MutableList<String> = mutableListOf()
            this.lore.forEach { loreList.add("${it.key}:${it.value}") }
            val loreStorage = LoreStorage(loreList)
            pdc.set(ToolBox.LORE_KEY, PersistentDataType.BYTE_ARRAY, loreStorage.toByteArray())

            val enchantmentLores: MutableList<String> = mutableListOf()
            val lores: MutableList<String> = mutableListOf()
            lore.forEach {
                if (it.key.startsWith("enchantment")) {
                    enchantmentLores.add(it.value)
                } else {
                    lores.add(it.value)
                }
            }



            val LoresCompons: MutableList<Component> = mutableListOf()

            // Enchantments If there is an enchantment* key the enchantments lore section will be added.
            if (enchantmentLores.isNotEmpty()) {
                LoresCompons.add(MiniMessage.miniMessage().deserialize("<gold>----- <bold>Enchantments</bold> -----</gold>"))
                LoresCompons.add(MiniMessage.miniMessage().deserialize("<reset>"))
                enchantmentLores.forEach {
                    LoresCompons.add(MiniMessage.miniMessage().deserialize(it))
                }
            }


            if (lores.isNotEmpty()) {
                LoresCompons.add(MiniMessage.miniMessage().deserialize("<gold>----- <bold>Information</bold> -----</gold>"))
                LoresCompons.add(MiniMessage.miniMessage().deserialize("<reset>"))

                lores.forEach {
                    LoresCompons.add(MiniMessage.miniMessage().deserialize(it))
                }
            }
            itemMeta.lore(LoresCompons)
            item.itemMeta = itemMeta


        }


    }

}

