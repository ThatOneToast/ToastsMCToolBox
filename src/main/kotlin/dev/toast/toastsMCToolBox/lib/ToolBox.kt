package dev.toast.toastsMCToolBox.lib

import dev.toast.toastsMCToolBox.lib.commands.GetDebugStick
import dev.toast.toastsMCToolBox.lib.commands.RPSkills
import dev.toast.toastsMCToolBox.lib.commands.SetLoreOf
import dev.toast.toastsMCToolBox.lib.commands.Skills
import dev.toast.toastsMCToolBox.lib.extras.TDebugStick
import dev.toast.toastsMCToolBox.lib.extras.listeners.Caller
import dev.toast.toastsMCToolBox.lib.files.FileKit
import dev.toast.toastsMCToolBox.lib.items.ItemKit
import dev.toast.toastsMCToolBox.lib.lore.LoreKit
import dev.toast.toastsMCToolBox.lib.rpx.RPXKit
import org.bukkit.NamespacedKey
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin
import kotlin.random.Random

class ToolBox(private val plugin: Plugin) {


    private val listOfListeners = mutableListOf(
        Caller,
        TDebugStick,
        RPXKit,
        ItemKit,

    )

    init {
        instance = plugin
        playerStorageUnitsPath = "${instance.dataFolder.absolutePath}/PSUs/"
        taggedStoragePath = "${instance.dataFolder.absolutePath}/TSUs/"
        LORE_KEY = NamespacedKey(plugin, "lore")
        TAG_KEY = NamespacedKey(plugin, "identifier")
        RACE_KEY = NamespacedKey(instance, "race")
        PLAYER_CLASS_LEVEL = NamespacedKey(instance, "player_class_level")
        PLAYER_CLASS_MAX_LEVEL = NamespacedKey(instance, "player_class_max_level")
        PLAYER_CLASS_EXPERIENCE = NamespacedKey(instance, "player_class_experience")
        PLAYER_CLASS_EXPERIENCE_NEEDED = NamespacedKey(instance, "player_class_experience_needed")
        PLAYER_CLASS_SKILLS = NamespacedKey(instance, "player_class_skills")
        ITEM_HANDLER_KEY = NamespacedKey(instance, "item_handler")

        GetDebugStick()
        SetLoreOf()
        RPSkills()
        Skills()

        listOfListeners.forEach {
            instance.server.pluginManager.registerEvents(it, instance)
        }
    }



    companion object {
        private lateinit var instance: Plugin
        private val loreKit = LoreKit
        private val fileKit = FileKit

        lateinit var playerStorageUnitsPath: String
        lateinit var taggedStoragePath: String

        lateinit var LORE_KEY: NamespacedKey
        lateinit var TAG_KEY: NamespacedKey
        lateinit var RACE_KEY: NamespacedKey
        lateinit var PLAYER_CLASS_LEVEL: NamespacedKey
        lateinit var PLAYER_CLASS_MAX_LEVEL: NamespacedKey
        lateinit var PLAYER_CLASS_EXPERIENCE: NamespacedKey
        lateinit var PLAYER_CLASS_EXPERIENCE_NEEDED: NamespacedKey
        lateinit var PLAYER_CLASS_SKILLS: NamespacedKey
        lateinit var ITEM_HANDLER_KEY: NamespacedKey


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

        @JvmStatic
        fun getFileKit(): FileKit {
            return fileKit
        }

    }
}

fun registerEvent(event: Listener) {
    ToolBox.getPlugin().server.pluginManager.registerEvents(event, ToolBox.getPlugin())
}
fun String.prettify(): String {
    val lines = split("\n")
    val prettified = lines.joinToString("\n") { it.trim() }
    return prettified
}

fun randomBitString(length: Int): String {
    val chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
    val random = Random.Default
    return (1..length)
        .map { random.nextInt(chars.length) }
        .map(chars::get)
        .joinToString("")
}