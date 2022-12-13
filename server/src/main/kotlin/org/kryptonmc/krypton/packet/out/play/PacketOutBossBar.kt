/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
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
import net.kyori.adventure.bossbar.BossBar.Color
import net.kyori.adventure.bossbar.BossBar.Overlay
import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.network.Writable
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.util.readComponent
import org.kryptonmc.krypton.util.readEnum
import org.kryptonmc.krypton.util.readUUID
import org.kryptonmc.krypton.util.readVarInt
import org.kryptonmc.krypton.util.writeComponent
import org.kryptonmc.krypton.util.writeEnum
import org.kryptonmc.krypton.util.writeUUID
import org.kryptonmc.krypton.util.writeVarInt
import java.util.UUID

@JvmRecord
data class PacketOutBossBar(val uuid: UUID, val action: Action) : Packet {

    constructor(buf: ByteBuf) : this(buf.readUUID(), when (buf.readEnum<ActionType>()) {
        ActionType.ADD -> AddAction(buf)
        ActionType.REMOVE -> RemoveAction
        ActionType.UPDATE_PROGRESS -> UpdateProgressAction(buf.readFloat())
        ActionType.UPDATE_TITLE -> UpdateTitleAction(buf.readComponent())
        ActionType.UPDATE_STYLE -> UpdateStyleAction(COLORS[buf.readVarInt()], OVERLAYS[buf.readVarInt()])
        ActionType.UPDATE_FLAGS -> UpdateFlagsAction(buf.readByte().toInt())
    })

    override fun write(buf: ByteBuf) {
        buf.writeUUID(uuid)
        buf.writeEnum(action.type())
        action.write(buf)
    }

    sealed interface Action : Writable {

        fun type(): ActionType
    }

    @JvmRecord
    data class AddAction(val name: Component, val progress: Float, val color: Color, val overlay: Overlay, val flags: Int) : Action {

        constructor(bar: BossBar) : this(bar.name(), bar.progress(), bar.color(), bar.overlay(), encodeFlags(bar))

        constructor(buf: ByteBuf) : this(buf.readComponent(), buf.readFloat(), readColor(buf), readOverlay(buf), buf.readByte().toInt())

        override fun type(): ActionType = ActionType.ADD

        override fun write(buf: ByteBuf) {
            buf.writeComponent(name)
            buf.writeFloat(progress)
            buf.writeVarInt(color.ordinal)
            buf.writeVarInt(overlay.ordinal)
            buf.writeByte(flags)
        }
    }

    object RemoveAction : Action {

        override fun type(): ActionType = ActionType.REMOVE

        override fun write(buf: ByteBuf) {
            // Nothing
        }
    }

    @JvmRecord
    data class UpdateProgressAction(val progress: Float) : Action {

        override fun type(): ActionType = ActionType.UPDATE_PROGRESS

        override fun write(buf: ByteBuf) {
            buf.writeFloat(progress)
        }
    }

    @JvmRecord
    data class UpdateTitleAction(val name: Component) : Action {

        override fun type(): ActionType = ActionType.UPDATE_TITLE

        override fun write(buf: ByteBuf) {
            buf.writeComponent(name)
        }
    }

    @JvmRecord
    data class UpdateStyleAction(val color: Color, val overlay: Overlay) : Action {

        override fun type(): ActionType = ActionType.UPDATE_STYLE

        override fun write(buf: ByteBuf) {
            buf.writeVarInt(color.ordinal)
            buf.writeVarInt(overlay.ordinal)
        }
    }

    @JvmRecord
    data class UpdateFlagsAction(val flags: Int) : Action {

        constructor(flags: Set<BossBar.Flag>) : this(flags.fold(0) { acc, flag -> acc or flag.ordinal })

        override fun type(): ActionType = ActionType.UPDATE_FLAGS

        override fun write(buf: ByteBuf) {
            buf.writeByte(flags)
        }
    }

    enum class ActionType {

        ADD,
        REMOVE,
        UPDATE_PROGRESS,
        UPDATE_TITLE,
        UPDATE_STYLE,
        UPDATE_FLAGS
    }

    companion object {

        private val COLORS = Color.values()
        private val OVERLAYS = Overlay.values()

        @JvmStatic
        private fun readColor(buf: ByteBuf): Color = COLORS[buf.readVarInt()]

        @JvmStatic
        private fun readOverlay(buf: ByteBuf): Overlay = OVERLAYS[buf.readVarInt()]

        @JvmStatic
        private fun encodeFlags(bar: BossBar): Int {
            var byte = 0
            if (bar.hasFlag(BossBar.Flag.DARKEN_SCREEN)) byte = byte or 1
            if (bar.hasFlag(BossBar.Flag.PLAY_BOSS_MUSIC)) byte = byte or 2
            if (bar.hasFlag(BossBar.Flag.CREATE_WORLD_FOG)) byte = byte or 4
            return byte
        }
    }
}
