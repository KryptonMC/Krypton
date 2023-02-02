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
package org.kryptonmc.krypton.network.chat

import io.netty.buffer.ByteBuf
import net.kyori.adventure.chat.ChatType
import net.kyori.adventure.text.Component
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.krypton.command.CommandSourceStack
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.network.Writable
import org.kryptonmc.krypton.registry.KryptonDynamicRegistries
import org.kryptonmc.krypton.util.readComponent
import org.kryptonmc.krypton.util.readNullable
import org.kryptonmc.krypton.util.readVarInt
import org.kryptonmc.krypton.util.writeComponent
import org.kryptonmc.krypton.util.writeNullable
import org.kryptonmc.krypton.util.writeVarInt
import org.kryptonmc.serialization.Codec
import org.kryptonmc.serialization.codecs.RecordCodecBuilder

/**
 * Named as such to distinguish it from Adventure's ChatType.
 */
@JvmRecord
data class RichChatType(val chat: ChatTypeDecoration, val narration: ChatTypeDecoration) {

    fun bind(name: Component): Bound = Bound(this, name)

    @JvmRecord
    data class Bound(val chatType: RichChatType, val name: Component, val targetName: Component? = null) {

        fun decorate(message: Component): Component = chatType.chat.decorate(message, this)

        fun decorateNarration(message: Component): Component = chatType.narration.decorate(message, this)

        fun withTargetName(targetName: Component): Bound = Bound(chatType, name, targetName)

        fun toNetwork(): BoundNetwork = BoundNetwork(KryptonDynamicRegistries.CHAT_TYPE.getId(chatType), name, targetName)

        companion object {

            @JvmStatic
            fun from(adventure: ChatType.Bound): Bound {
                val type = checkNotNull(KryptonDynamicRegistries.CHAT_TYPE.get(adventure.type().key())) {
                    "Could not find corresponding rich chat type for Adventure type ${adventure.type()}!"
                }
                return Bound(type, adventure.name(), adventure.target())
            }
        }
    }

    @JvmRecord
    data class BoundNetwork(val chatType: Int, val name: Component, val targetName: Component?) : Writable {

        constructor(buf: ByteBuf) : this(buf.readVarInt(), buf.readComponent(), buf.readNullable(ByteBuf::readComponent))

        override fun write(buf: ByteBuf) {
            buf.writeVarInt(chatType)
            buf.writeComponent(name)
            buf.writeNullable(targetName, ByteBuf::writeComponent)
        }
    }

    companion object {

        @JvmField
        val CODEC: Codec<RichChatType> = RecordCodecBuilder.create { instance ->
            instance.group(
                ChatTypeDecoration.CODEC.fieldOf("chat").getting { it.chat },
                ChatTypeDecoration.CODEC.fieldOf("narration").getting { it.narration }
            ).apply(instance, ::RichChatType)
        }

        @JvmStatic
        fun bind(key: ResourceKey<RichChatType>, entity: KryptonEntity): Bound = bind(key, entity.displayName)

        @JvmStatic
        fun bind(key: ResourceKey<RichChatType>, source: CommandSourceStack): Bound = bind(key, source.displayName)

        @JvmStatic
        fun bind(key: ResourceKey<RichChatType>, name: Component): Bound = KryptonDynamicRegistries.CHAT_TYPE.getOrThrow(key).bind(name)
    }
}
