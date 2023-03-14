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
package org.kryptonmc.krypton.entity.serializer.vehicle

import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.vehicle.KryptonCommandBlockMinecart
import org.kryptonmc.nbt.CompoundTag

object CommandBlockMinecartSerializer : EntitySerializer<KryptonCommandBlockMinecart> {

    override fun load(entity: KryptonCommandBlockMinecart, data: CompoundTag) {
        MinecartLikeSerializer.load(entity, data)
        entity.commandBlock.load(data)
        entity.command = entity.commandBlock.command
        entity.lastOutput = entity.commandBlock.lastOutput ?: Component.empty()
    }

    override fun save(entity: KryptonCommandBlockMinecart): CompoundTag.Builder = MinecartLikeSerializer.save(entity).apply {
        entity.commandBlock.save(this)
    }
}
