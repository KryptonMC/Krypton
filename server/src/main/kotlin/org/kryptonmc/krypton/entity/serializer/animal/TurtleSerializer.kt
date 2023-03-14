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
package org.kryptonmc.krypton.entity.serializer.animal

import org.kryptonmc.krypton.entity.animal.KryptonTurtle
import org.kryptonmc.krypton.entity.serializer.AgeableSerializer
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.util.nbt.getBlockPos
import org.kryptonmc.krypton.util.nbt.putBlockPosParts
import org.kryptonmc.nbt.CompoundTag

object TurtleSerializer : EntitySerializer<KryptonTurtle> {

    private const val HOME_PREFIX = "HomePos"
    private const val DESTINATION_PREFIX = "TravelPos"
    private const val HAS_EGG_TAG = "HasEgg"

    override fun load(entity: KryptonTurtle, data: CompoundTag) {
        AgeableSerializer.load(entity, data)
        entity.home = data.getBlockPos(HOME_PREFIX)
        entity.destination = data.getBlockPos(DESTINATION_PREFIX)
        entity.hasEgg = data.getBoolean(HAS_EGG_TAG)
    }

    override fun save(entity: KryptonTurtle): CompoundTag.Builder = AgeableSerializer.save(entity).apply {
        putBlockPosParts(entity.home, HOME_PREFIX)
        putBlockPosParts(entity.destination, DESTINATION_PREFIX)
        putBoolean(HAS_EGG_TAG, entity.hasEgg)
    }
}
