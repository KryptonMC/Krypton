package org.kryptonmc.krypton.world.bossbar

import com.google.common.collect.MapMaker
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.entity.entities.KryptonPlayer
import org.kryptonmc.krypton.packet.out.play.BossBarAction
import org.kryptonmc.krypton.packet.out.play.BossBarAction.UPDATE_FLAGS
import org.kryptonmc.krypton.packet.out.play.BossBarAction.UPDATE_HEALTH
import org.kryptonmc.krypton.packet.out.play.BossBarAction.UPDATE_STYLE
import org.kryptonmc.krypton.packet.out.play.BossBarAction.UPDATE_TITLE
import org.kryptonmc.krypton.packet.out.play.PacketOutBossBar
import java.util.Collections
import java.util.UUID

private val BARS: MutableMap<BossBar, BossBarManager.BossBarHolder> = MapMaker().weakKeys().makeMap()

object BossBarManager : BossBar.Listener, MutableMap<BossBar, BossBarManager.BossBarHolder> by BARS {

    private fun getOrCreate(bar: BossBar) = BARS.getOrPut(bar) { BossBarHolder(bar) }.apply { register() }

    fun addBar(bar: BossBar, player: KryptonPlayer) {
        getOrCreate(bar).apply { if (subscribers.add(player)) player.session.sendPacket(PacketOutBossBar(BossBarAction.ADD, this)) }
    }

    fun removeBar(bar: BossBar, player: KryptonPlayer) {
        get(bar)?.apply { if (subscribers.remove(player)) player.session.sendPacket(PacketOutBossBar(BossBarAction.REMOVE, this)) }
    }

    override fun bossBarNameChanged(bar: BossBar, oldName: Component, newName: Component) =
        update(bar, UPDATE_TITLE)

    override fun bossBarProgressChanged(bar: BossBar, oldProgress: Float, newProgress: Float) =
        update(bar, UPDATE_HEALTH)

    override fun bossBarColorChanged(bar: BossBar, oldColor: BossBar.Color, newColor: BossBar.Color) =
        update(bar, UPDATE_STYLE)

    override fun bossBarOverlayChanged(bar: BossBar, oldOverlay: BossBar.Overlay, newOverlay: BossBar.Overlay) =
        update(bar, UPDATE_STYLE)

    override fun bossBarFlagsChanged(bar: BossBar, flagsAdded: MutableSet<BossBar.Flag>, flagsRemoved: MutableSet<BossBar.Flag>) =
        update(bar, UPDATE_FLAGS)

    private fun update(bar: BossBar, action: BossBarAction) {
        val holder = get(bar) ?: return
        holder.subscribers.forEach { it.session.sendPacket(PacketOutBossBar(action, holder)) }
    }

    class BossBarHolder(val bar: BossBar) {

        val id: UUID = UUID.randomUUID()
        val subscribers: MutableSet<KryptonPlayer> = Collections.newSetFromMap(MapMaker().weakKeys().makeMap())

        fun register() {
            bar.addListener(BossBarManager)
        }
    }
}
