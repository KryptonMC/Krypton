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
package org.kryptonmc.krypton.util.nbt

import org.kryptonmc.krypton.util.uuid.UUIDUtil
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.IntArrayTag
import org.kryptonmc.nbt.ListTag
import java.util.UUID

fun CompoundTag.hasUUID(name: String): Boolean {
    val tag = get(name)
    return tag != null && tag.id() == IntArrayTag.ID && (tag as IntArrayTag).data.size == 4
}

fun CompoundTag.getUUID(name: String): UUID = UUIDUtil.loadUUID(get(name)!!)

fun CompoundTag.Builder.putUUID(name: String, uuid: UUID): CompoundTag.Builder = put(name, UUIDUtil.createUUID(uuid))

fun ListTag.Builder.addUUID(uuid: UUID): ListTag.Builder = add(UUIDUtil.createUUID(uuid))
