package dev.toast.toastsMCToolBox.lib.rpx.mana

import dev.toast.toastsMCToolBox.lib.ToolBox
import dev.toast.toastsMCToolBox.lib.overrides.players.ManaToolBox
import dev.toast.toastsMCToolBox.lib.overrides.players.manaToolBox
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable

object ManaKit {


    fun startManaScheduler() {
        object : BukkitRunnable() {
            override fun run() {
                val players = Bukkit.getOnlinePlayers()
                for (player in players) {
                    try {
                        player.manaToolBox.giveMana(player.manaToolBox.manaPerSecond)
                    } catch (_: ManaToolBox.ManaOverflowException) {}
                }
            }
        }.runTaskTimer(ToolBox.getPlugin(), 0L, 20L) // 20 ticks = 1 second
    }


}