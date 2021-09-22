/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import net.kyori.adventure.bossbar.BossBar
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.util.writeChat
import org.kryptonmc.krypton.util.writeUUID
import org.kryptonmc.krypton.util.writeVarInt
import org.kryptonmc.krypton.util.BossBarManager

@JvmRecord
data class PacketOutBossBar(
    private val action: BossBarAction,
    private val bar: BossBarManager.BossBarHolder
) : Packet {

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
