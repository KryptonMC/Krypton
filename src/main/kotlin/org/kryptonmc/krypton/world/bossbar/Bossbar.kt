package org.kryptonmc.krypton.world.bossbar

import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.lang.LegacyColor
import org.kryptonmc.krypton.registry.NamespacedKey
import java.util.*

// We wrap a lot of Adventure's boss bar stuff here so we can include the ID, visibility and player list
data class Bossbar(
    val id: NamespacedKey,
    val name: Component,
    val color: BossBar.Color,
    val overlay: BossBar.Overlay,
    val progress: Float,
    val flags: Set<BossBar.Flag>,
    val visible: Boolean,
    val players: List<UUID>
)