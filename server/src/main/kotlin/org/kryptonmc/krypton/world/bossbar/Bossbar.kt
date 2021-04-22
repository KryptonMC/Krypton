package org.kryptonmc.krypton.world.bossbar

import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.api.registry.NamespacedKey
import java.util.*

/**
 * A boss bar.
 * Notice that we wrap a lot of Adventure's boss bar stuff here, but we use this so we can include the ID,
 * visibility and player list in this object
 *
 * @author Callum Seabrook
 */
@Suppress("NonExtendableApiUsage")
data class Bossbar(
    val wrapped: BossBar,
    val id: NamespacedKey,
    val visible: Boolean,
    val players: List<UUID>
) : BossBar by wrapped