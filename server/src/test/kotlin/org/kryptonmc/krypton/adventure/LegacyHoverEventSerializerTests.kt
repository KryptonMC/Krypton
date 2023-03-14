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
package org.kryptonmc.krypton.adventure

import net.kyori.adventure.key.Key
import net.kyori.adventure.nbt.api.BinaryTagHolder
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.kyori.adventure.util.Codec
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.kryptonmc.krypton.adventure.provider.NBTLegacyHoverEventSerializer
import java.io.IOException
import java.util.UUID
import kotlin.test.assertEquals

class LegacyHoverEventSerializerTests {

    @Test
    fun `ensure full legacy show item deserializes to correct modern event`() {
        val nbt = "{CustomModelData:74,Damage:34,Unbreakable:1b}"
        val input = Component.text("{Count:37B,id:'minecraft:air',tag:$nbt}")
        val item = HoverEvent.ShowItem.of(Key.key("air"), 37, BinaryTagHolder.binaryTagHolder(nbt))
        assertEquals(item, NBTLegacyHoverEventSerializer.deserializeShowItem(input))
    }

    @Test
    fun `ensure legacy show item with no nbt deserializes to correct modern event`() {
        val noTag = Component.text("{Count:37B,id:'minecraft:air'}")
        val item = HoverEvent.ShowItem.of(Key.key("air"), 37, null)
        assertEquals(item, NBTLegacyHoverEventSerializer.deserializeShowItem(noTag))
    }

    @Test
    fun `ensure legacy show item with invalid nbt fails to deserializes`() {
        val badNbt = Component.text("{Count:37B,id:'minecraft:air',tag:hello world}")
        assertThrows<IOException> { NBTLegacyHoverEventSerializer.deserializeShowItem(badNbt) }
    }

    @Test
    fun `ensure full modern show item serializes to correct legacy event`() {
        val nbt = "{CustomModelData:74,Damage:34,Unbreakable:1b}"
        val item = HoverEvent.ShowItem.of(Key.key("air"), 37, BinaryTagHolder.binaryTagHolder(nbt))
        val output = Component.text("{id:\"minecraft:air\",Count:37b,tag:$nbt}")
        assertEquals(output, NBTLegacyHoverEventSerializer.serializeShowItem(item))
    }

    @Test
    fun `ensure modern show item with no nbt serializes to correct legacy event`() {
        val noTag = Component.text("{id:\"minecraft:air\",Count:37b}")
        val item = HoverEvent.ShowItem.of(Key.key("air"), 37, null)
        assertEquals(noTag, NBTLegacyHoverEventSerializer.serializeShowItem(item))
    }

    @Test
    fun `ensure modern show item with invalid nbt fails to serialize`() {
        val item = HoverEvent.ShowItem.of(Key.key("air"), 37, BinaryTagHolder.binaryTagHolder("hello world"))
        assertThrows<IOException> { NBTLegacyHoverEventSerializer.serializeShowItem(item) }
    }

    @Test
    fun `ensure full legacy show entity deserializes to correct modern event`() {
        val uuid = UUID.fromString("aaaabbbb-cccc-dddd-eeee-ffffaaaabbbb")
        val name = Component.text("What a name")
        val input = Component.text("{type:'minecraft:pig',id:'$uuid',name:'${GsonComponentSerializer.gson().serialize(name)}'}")
        val entity = HoverEvent.ShowEntity.of(Key.key("pig"), uuid, name)
        assertEquals(entity, NBTLegacyHoverEventSerializer.deserializeShowEntity(input, SIMPLE_JSON_DECODER))
    }

    @Test
    fun `ensure legacy show entity with invalid nbt fails to deserialize`() {
        val bogus = Component.text("hello world")
        assertThrows<IOException> { NBTLegacyHoverEventSerializer.deserializeShowEntity(bogus, SIMPLE_JSON_DECODER) }
    }

    @Test
    fun `ensure full modern show entity serializes to correct legacy event`() {
        val uuid = UUID.fromString("aaaabbbb-cccc-dddd-eeee-ffffaaaabbbb")
        val name = Component.text("What a name")
        val output = Component.text("{id:\"$uuid\",type:\"minecraft:pig\",name:'${GsonComponentSerializer.gson().serialize(name)}'}")
        val entity = HoverEvent.ShowEntity.of(Key.key("pig"), uuid, name)
        assertEquals(output, NBTLegacyHoverEventSerializer.serializeShowEntity(entity, SIMPLE_JSON_ENCODER))
    }

    companion object {

        // Used as dummies for show entity, where we require a codec to encode/decode the entity's name.
        // We don't really care what these actually do, we just need something that's simple and dependable, and the GSON serializer
        // meets both of those requirements.
        private val SIMPLE_JSON_ENCODER = Codec.Encoder<Component, String, RuntimeException>(GsonComponentSerializer.gson()::serialize)
        private val SIMPLE_JSON_DECODER = Codec.Decoder<Component, String, RuntimeException>(GsonComponentSerializer.gson()::deserialize)
    }
}
