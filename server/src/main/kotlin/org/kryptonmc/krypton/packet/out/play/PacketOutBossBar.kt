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
import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.util.writeChat
import org.kryptonmc.krypton.util.writeUUID
import org.kryptonmc.krypton.util.writeVarInt
import org.kryptonmc.krypton.util.BossBarManager
import java.util.UUID

@JvmRecord
data class PacketOutBossBar(
    val action: Action,
    val uuid: UUID,
    val name: Component,
    val progress: Float,
    val color: BossBar.Color,
    val overlay: BossBar.Overlay,
    val flags: Int
) : Packet {

    constructor(action: Action, bar: BossBarManager.BossBarHolder) : this(action, bar.id, bar.bar)

    constructor(action: Action, uuid: UUID, bar: BossBar) : this(
        action,
        uuid,
        bar.name(),
        bar.progress(),
        bar.color(),
        bar.overlay(),
        flagsToProtocol(bar)
    )

    override fun write(buf: ByteBuf) {
        buf.writeUUID(uuid)
        buf.writeVarInt(action.ordinal)

        when (action) {
            Action.ADD -> {
                buf.writeChat(name)
                buf.writeFloat(progress)
                buf.writeVarInt(color.ordinal)
                buf.writeVarInt(overlay.ordinal)
                buf.writeByte(flags)
            }
            Action.REMOVE -> Unit
            Action.UPDATE_HEALTH -> buf.writeFloat(progress)
            Action.UPDATE_TITLE -> buf.writeChat(name)
            Action.UPDATE_STYLE -> {
                buf.writeVarInt(color.ordinal)
                buf.writeVarInt(overlay.ordinal)
            }
            Action.UPDATE_FLAGS -> buf.writeByte(flags)
        }
    }

    enum class Action {

        ADD,
        REMOVE,
        UPDATE_HEALTH,
        UPDATE_TITLE,
        UPDATE_STYLE,
        UPDATE_FLAGS;

        companion object {

            private val BY_ID = values()

            @JvmStatic
            fun fromId(id: Int): Action? = BY_ID.getOrNull(id)
        }
    }

    companion object {

        @JvmStatic
        private fun flagsToProtocol(bar: BossBar): Int {
            var byte = 0
            if (bar.hasFlag(BossBar.Flag.DARKEN_SCREEN)) byte = byte or 1
            if (bar.hasFlag(BossBar.Flag.PLAY_BOSS_MUSIC)) byte = byte or 2
            if (bar.hasFlag(BossBar.Flag.CREATE_WORLD_FOG)) byte = byte or 4
            return byte
        }
    }
}
