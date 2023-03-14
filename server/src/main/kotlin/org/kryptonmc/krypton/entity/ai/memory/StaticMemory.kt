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
package org.kryptonmc.krypton.entity.ai.memory

import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.compound
import org.kryptonmc.serialization.nbt.NbtOps

// A static memory is a memory that does not expire.
class StaticMemory<T : Any>(override val value: T?) : Memory<T> {

    override fun tick() {
        // Do nothing - no expiry for static memories
    }

    override fun save(key: MemoryKey<in T>, data: CompoundTag.Builder): CompoundTag.Builder {
        return data.compound(key.key().asString()) {
            if (value != null) put("value", key.codec.encodeStart(value, NbtOps.INSTANCE).result().get())
        }
    }

    override fun toString(): String = "StaticMemory(value=$value)"
}
