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

import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.bossbar.BossBar.Color
import net.kyori.adventure.bossbar.BossBar.Overlay
import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.network.Writable
import org.kryptonmc.krypton.network.buffer.BinaryReader
import org.kryptonmc.krypton.network.buffer.BinaryWriter
import org.kryptonmc.krypton.packet.Packet
import java.util.UUID

@JvmRecord
data class PacketOutBossBar(val uuid: UUID, val action: Action) : Packet {

    constructor(reader: BinaryReader) : this(reader.readUUID(), reader.readEnum<ActionType>().read(reader))

    override fun write(writer: BinaryWriter) {
        writer.writeUUID(uuid)
        writer.writeEnum(action.type())
        action.write(writer)
    }

    sealed interface Action : Writable {

        fun type(): ActionType
    }

    @JvmRecord
    data class AddAction(val name: Component, val progress: Float, val color: Color, val overlay: Overlay, val flags: Byte) : Action {

        constructor(bar: BossBar) : this(bar.name(), bar.progress(), bar.color(), bar.overlay(), encodeFlags(bar))

        override fun type(): ActionType = ActionType.ADD

        override fun write(writer: BinaryWriter) {
            writer.writeComponent(name)
            writer.writeFloat(progress)
            writer.writeVarInt(color.ordinal)
            writer.writeVarInt(overlay.ordinal)
            writer.writeByte(flags)
        }
    }

    object RemoveAction : Action {

        override fun type(): ActionType = ActionType.REMOVE

        override fun write(writer: BinaryWriter) {
            // Nothing
        }
    }

    @JvmRecord
    data class UpdateProgressAction(val progress: Float) : Action {

        override fun type(): ActionType = ActionType.UPDATE_PROGRESS

        override fun write(writer: BinaryWriter) {
            writer.writeFloat(progress)
        }
    }

    @JvmRecord
    data class UpdateNameAction(val name: Component) : Action {

        override fun type(): ActionType = ActionType.UPDATE_NAME

        override fun write(writer: BinaryWriter) {
            writer.writeComponent(name)
        }
    }

    @JvmRecord
    data class UpdateStyleAction(val color: Color, val overlay: Overlay) : Action {

        override fun type(): ActionType = ActionType.UPDATE_STYLE

        override fun write(writer: BinaryWriter) {
            writer.writeEnum(color)
            writer.writeEnum(overlay)
        }
    }

    @JvmRecord
    data class UpdateFlagsAction(val flags: Byte) : Action {

        constructor(flags: Set<BossBar.Flag>) : this(flags.fold(0) { acc, flag -> acc or flag.ordinal }.toByte())

        override fun type(): ActionType = ActionType.UPDATE_FLAGS

        override fun write(writer: BinaryWriter) {
            writer.writeByte(flags)
        }
    }

    enum class ActionType(private val reader: Reader) {

        ADD({ AddAction(it.readComponent(), it.readFloat(), readColor(it), readOverlay(it), it.readByte()) }),
        REMOVE({ RemoveAction }),
        UPDATE_PROGRESS({ UpdateProgressAction(it.readFloat()) }),
        UPDATE_NAME({ UpdateNameAction(it.readComponent()) }),
        UPDATE_STYLE({ UpdateStyleAction(readColor(it), readOverlay(it)) }),
        UPDATE_FLAGS({ UpdateFlagsAction(it.readByte()) });

        fun read(reader: BinaryReader): Action = this.reader.read(reader)

        private fun interface Reader {

            fun read(reader: BinaryReader): Action
        }
    }

    companion object {

        private val COLORS = Color.values()
        private val OVERLAYS = Overlay.values()

        @JvmStatic
        private fun readColor(reader: BinaryReader): Color = COLORS[reader.readVarInt()]

        @JvmStatic
        private fun readOverlay(reader: BinaryReader): Overlay = OVERLAYS[reader.readVarInt()]

        @JvmStatic
        private fun encodeFlags(bar: BossBar): Byte {
            var byte = 0
            if (bar.hasFlag(BossBar.Flag.DARKEN_SCREEN)) byte = byte or 1
            if (bar.hasFlag(BossBar.Flag.PLAY_BOSS_MUSIC)) byte = byte or 2
            if (bar.hasFlag(BossBar.Flag.CREATE_WORLD_FOG)) byte = byte or 4
            return byte.toByte()
        }
    }
}
