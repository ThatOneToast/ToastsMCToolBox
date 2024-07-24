package dev.toast.toastsMCToolBox.lib

import dev.toast.toastsMCToolBox.lib.lore.LoreKit
import org.bukkit.NamespacedKey
import org.bukkit.plugin.Plugin

class  ToolBox(private val plugin: Plugin) {

    init {
        instance = plugin
        LORE_KEY = NamespacedKey(plugin, "lore")
    }



    companion object {
        private lateinit var instance: Plugin
        private val loreKit = LoreKit()

        lateinit var LORE_KEY: NamespacedKey


        @JvmStatic
        fun getPlugin(): Plugin {
            if (::instance.isInitialized) {
                return instance
            } else throw IllegalStateException("Plugin instance not initialized")
        }

        @JvmStatic
        fun getLoreKit(): LoreKit {
            return loreKit
        }
    }
}