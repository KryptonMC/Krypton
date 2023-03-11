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
import net.kyori.adventure.key.Key
import org.kryptonmc.krypton.entity.player.RecipeBookSettings
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.util.readEnum
import org.kryptonmc.krypton.util.readKey
import org.kryptonmc.krypton.util.readList
import org.kryptonmc.krypton.util.writeCollection
import org.kryptonmc.krypton.util.writeEnum
import org.kryptonmc.krypton.util.writeKey

@JvmRecord
data class PacketOutUpdateRecipeBook(
    val action: Action,
    val recipes: List<Key>,
    val toHighlight: List<Key>,
    val settings: RecipeBookSettings
) : Packet {

    constructor(buf: ByteBuf) : this(buf, buf.readEnum(), RecipeBookSettings.read(buf))

    private constructor(buf: ByteBuf, action: Action, settings: RecipeBookSettings) : this(action, buf.readList(ByteBuf::readKey),
        if (action == Action.INIT) buf.readList(ByteBuf::readKey) else emptyList(), settings)

    override fun write(buf: ByteBuf) {
        buf.writeEnum(action)
        settings.write(buf)
        buf.writeCollection(recipes, buf::writeKey)
        if (action == Action.INIT) buf.writeCollection(toHighlight, buf::writeKey)
    }

    enum class Action {

        INIT,
        ADD,
        REMOVE
    }
}
