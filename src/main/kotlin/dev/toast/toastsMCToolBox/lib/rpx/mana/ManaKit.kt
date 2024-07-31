package dev.toast.toastsMCToolBox.lib.rpx.mana

import dev.toast.toastsMCToolBox.lib.ToolBox
import dev.toast.toastsMCToolBox.lib.callEvent
import dev.toast.toastsMCToolBox.lib.extras.listeners.ManaOverflowEvent
import dev.toast.toastsMCToolBox.lib.overrides.players.ManaToolBox
import dev.toast.toastsMCToolBox.lib.overrides.players.manaToolBox
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

object ManaKit {


    fun startManaScheduler() {
        object : BukkitRunnable() {
            val lastOverflow = mutableMapOf<UUID, Long>()

            /**
             * Returns true if the player was last overflowed 10 seconds ago
             */
            fun wasLastOverflow10SecondsAgo(uuid: UUID): Boolean {
                val lastOverflowTime = lastOverflow[uuid] ?: return false
                val currentTime = System.currentTimeMillis()
                return (currentTime - lastOverflowTime) < 10000
            }

            override fun run() {
                val players = Bukkit.getOnlinePlayers()
                for (player in players) {
                    try {
                        player.manaToolBox.giveMana(player.manaToolBox.manaPerSecond)
                    } catch (_: ManaToolBox.ManaOverflowException) {
                        if (!wasLastOverflow10SecondsAgo(player.uniqueId)) {
                            lastOverflow[player.uniqueId] = System.currentTimeMillis()
                            callEvent(ManaOverflowEvent(player))
                        }
                    }
                }
            }
        }.runTaskTimer(ToolBox.getPlugin(), 0L, 20L) // 20 ticks = 1 second
    }


}