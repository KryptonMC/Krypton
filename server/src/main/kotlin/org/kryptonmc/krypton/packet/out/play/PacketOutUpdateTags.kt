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

import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.krypton.network.buffer.BinaryReader
import org.kryptonmc.krypton.network.buffer.BinaryWriter
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.resource.KryptonResourceKeys
import org.kryptonmc.krypton.tags.TagSerializer

@JvmRecord
data class PacketOutUpdateTags(val tags: Map<ResourceKey<out Registry<*>>, TagSerializer.NetworkPayload>) : Packet {

    constructor(reader: BinaryReader) : this(readTags(reader))

    override fun write(writer: BinaryWriter) {
        writer.writeMap(tags, BinaryWriter::writeResourceKey) { w, payload -> payload.write(w) }
    }

    companion object {

        @JvmStatic
        private fun readTags(reader: BinaryReader): Map<ResourceKey<out Registry<*>>, TagSerializer.NetworkPayload> {
            return reader.readMap({ ResourceKey.of(KryptonResourceKeys.PARENT, it.readKey()) }, TagSerializer::NetworkPayload)
        }
    }
}
