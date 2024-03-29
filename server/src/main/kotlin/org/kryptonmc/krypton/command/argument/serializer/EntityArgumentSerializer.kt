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
package org.kryptonmc.krypton.command.argument.serializer

import org.kryptonmc.krypton.command.arguments.entities.EntityArgumentType
import org.kryptonmc.krypton.network.buffer.BinaryReader
import org.kryptonmc.krypton.network.buffer.BinaryWriter

/**
 * Entity argument types are serialized with flags indicating if the selector
 * has a single target, and if it only targets players.
 *
 * See [here](https://wiki.vg/Command_Data#minecraft:entity)
 */
object EntityArgumentSerializer : FlaggedArgumentSerializer<EntityArgumentType> {

    override fun read(reader: BinaryReader, flags: Int): EntityArgumentType = EntityArgumentType.from(flags and 1 != 0, flags and 2 != 0)

    override fun write(writer: BinaryWriter, value: EntityArgumentType) {
        writer.writeByte(createFlags(value.singleTarget, value.onlyPlayers))
    }
}
