package org.kryptonmc.krypton.world.bossbar

import me.bardy.komponent.Component
import org.kryptonmc.krypton.lang.LegacyColor
import org.kryptonmc.krypton.registry.NamespacedKey
import java.util.*

data class Bossbar(
    val id: NamespacedKey,
    val name: Component,
    val color: LegacyColor,
    val overlay: BossbarOverlay,
    val maxHealth: Int,
    val currentHealth: Int,
    val createWorldFog: Boolean,
    val darkenSky: Boolean,
    val playBossMusic: Boolean,
    val visible: Boolean,
    val players: List<UUID>
)

enum class BossbarOverlay {

    PROGRESS,
    NOTCHED_6,
    NOTCHED_10,
    NOTCHED_12,
    NOTCHED_20;

    override fun toString() = name.toLowerCase()
}