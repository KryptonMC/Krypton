/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

    constructor(buf: ByteBuf) : this(buf.readUUID(), buf.readEnum<ActionType>().read(buf))

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
    data class UpdateNameAction(val name: Component) : Action {

        override fun type(): ActionType = ActionType.UPDATE_NAME

        override fun write(buf: ByteBuf) {
            buf.writeComponent(name)
        }
    }

    @JvmRecord
    data class UpdateStyleAction(val color: Color, val overlay: Overlay) : Action {

        override fun type(): ActionType = ActionType.UPDATE_STYLE

        override fun write(buf: ByteBuf) {
            buf.writeEnum(color)
            buf.writeEnum(overlay)
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

    enum class ActionType(private val reader: Reader) {

        ADD({ AddAction(it.readComponent(), it.readFloat(), readColor(it), readOverlay(it), it.readByte().toInt()) }),
        REMOVE({ RemoveAction }),
        UPDATE_PROGRESS({ UpdateProgressAction(it.readFloat()) }),
        UPDATE_NAME({ UpdateNameAction(it.readComponent()) }),
        UPDATE_STYLE({ UpdateStyleAction(readColor(it), readOverlay(it)) }),
        UPDATE_FLAGS({ UpdateFlagsAction(it.readByte().toInt()) });

        fun read(buf: ByteBuf): Action = reader.read(buf)

        private fun interface Reader {

            fun read(buf: ByteBuf): Action
        }
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
