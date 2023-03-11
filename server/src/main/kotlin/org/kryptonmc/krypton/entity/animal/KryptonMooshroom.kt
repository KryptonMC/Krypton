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
package org.kryptonmc.krypton.entity.animal

import org.kryptonmc.api.entity.animal.Mooshroom
import org.kryptonmc.api.entity.animal.type.MooshroomVariant
import org.kryptonmc.krypton.entity.KryptonEntityType
import org.kryptonmc.krypton.entity.KryptonEntityTypes
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.serializer.animal.MooshroomSerializer
import org.kryptonmc.krypton.world.KryptonWorld

class KryptonMooshroom(world: KryptonWorld) : KryptonCow(world), Mooshroom {

    override val type: KryptonEntityType<KryptonMooshroom>
        get() = KryptonEntityTypes.MOOSHROOM
    override val serializer: EntitySerializer<KryptonMooshroom>
        get() = MooshroomSerializer

    override var variant: MooshroomVariant
        get() = deserializeType(data.get(MetadataKeys.Mooshroom.TYPE))
        set(value) = data.set(MetadataKeys.Mooshroom.TYPE, value.name.lowercase())

    override fun defineData() {
        super.defineData()
        data.define(MetadataKeys.Mooshroom.TYPE, MooshroomVariant.RED.name.lowercase())
    }

    companion object {

        private val TYPE_NAMES = MooshroomVariant.values().associateBy { it.name.lowercase() }

        @JvmStatic
        fun deserializeType(name: String): MooshroomVariant = TYPE_NAMES.getOrDefault(name, MooshroomVariant.RED)
    }
}
