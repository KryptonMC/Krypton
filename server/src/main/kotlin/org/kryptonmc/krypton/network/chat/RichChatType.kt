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
