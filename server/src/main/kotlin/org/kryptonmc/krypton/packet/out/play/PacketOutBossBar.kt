package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import net.kyori.adventure.bossbar.BossBar
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.util.writeChat
import org.kryptonmc.krypton.util.writeUUID
import org.kryptonmc.krypton.util.writeVarInt
import org.kryptonmc.krypton.world.bossbar.BossBarManager

class PacketOutBossBar(
    private val action: BossBarAction,
    private val bar: BossBarManager.BossBarHolder
) : PlayPacket(0x0C) {

    override fun write(buf: ByteBuf) {
        buf.writeUUID(bar.id)
        buf.writeVarInt(action.ordinal)

        when (action) {
            BossBarAction.ADD -> {
                buf.writeChat(bar.bar.name())
                buf.writeFloat(bar.bar.progress())
                buf.writeVarInt(bar.bar.color().ordinal)
                buf.writeVarInt(bar.bar.overlay().ordinal)
                buf.writeByte(bar.bar.flagsToProtocol())
            }
            BossBarAction.REMOVE -> Unit
            BossBarAction.UPDATE_HEALTH -> buf.writeFloat(bar.bar.progress())
            BossBarAction.UPDATE_TITLE -> buf.writeChat(bar.bar.name())
            BossBarAction.UPDATE_STYLE -> {
                buf.writeVarInt(bar.bar.color().ordinal)
                buf.writeVarInt(bar.bar.overlay().ordinal)
            }
            BossBarAction.UPDATE_FLAGS -> buf.writeByte(bar.bar.flagsToProtocol())
        }
    }

    private fun BossBar.flagsToProtocol(): Int {
        var byte = 0x0
        if (hasFlag(BossBar.Flag.DARKEN_SCREEN)) byte = byte or 0x01
        if (hasFlag(BossBar.Flag.PLAY_BOSS_MUSIC)) byte = byte or 0x02
        if (hasFlag(BossBar.Flag.CREATE_WORLD_FOG)) byte = byte or 0x04
        return byte
    }
}

enum class BossBarAction {

    ADD,
    REMOVE,
    UPDATE_HEALTH,
    UPDATE_TITLE,
    UPDATE_STYLE,
    UPDATE_FLAGS
}
