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

import org.kryptonmc.api.entity.animal.Sheep
import org.kryptonmc.api.item.data.DyeColor
import org.kryptonmc.krypton.entity.KryptonEntityType
import org.kryptonmc.krypton.entity.KryptonEntityTypes
import org.kryptonmc.krypton.entity.KryptonMob
import org.kryptonmc.krypton.entity.attribute.AttributeSupplier
import org.kryptonmc.krypton.entity.attribute.KryptonAttributeTypes
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.serializer.animal.SheepSerializer
import org.kryptonmc.krypton.util.enumhelper.DyeColors
import org.kryptonmc.krypton.world.KryptonWorld

class KryptonSheep(world: KryptonWorld) : KryptonAnimal(world), Sheep {

    override val type: KryptonEntityType<KryptonSheep>
        get() = KryptonEntityTypes.SHEEP
    override val serializer: EntitySerializer<KryptonSheep>
        get() = SheepSerializer

    override var isSheared: Boolean
        get() = data.getFlag(MetadataKeys.Sheep.FLAGS, FLAG_SHEARED)
        set(value) = data.setFlag(MetadataKeys.Sheep.FLAGS, FLAG_SHEARED, value)
    override var woolColor: DyeColor
        get() = DyeColors.fromId(data.get(MetadataKeys.Sheep.FLAGS).toInt() and WOOL_COLOR_MASK)
        set(value) {
            val old = data.get(MetadataKeys.Sheep.FLAGS).toInt()
            data.set(MetadataKeys.Sheep.FLAGS, (old and CLEAR_WOOL_COLOR_MASK or (value.ordinal and WOOL_COLOR_MASK)).toByte())
        }

    override fun defineData() {
        super.defineData()
        data.define(MetadataKeys.Sheep.FLAGS, 0)
    }

    companion object {

        private const val FLAG_SHEARED = 4
        private const val WOOL_COLOR_MASK = 0xF
        private const val CLEAR_WOOL_COLOR_MASK = 0xF0

        private const val DEFAULT_MAX_HEALTH = 8.0
        private const val DEFAULT_MOVEMENT_SPEED = 0.23

        @JvmStatic
        fun attributes(): AttributeSupplier.Builder = KryptonMob.attributes()
            .add(KryptonAttributeTypes.MAX_HEALTH, DEFAULT_MAX_HEALTH)
            .add(KryptonAttributeTypes.MOVEMENT_SPEED, DEFAULT_MOVEMENT_SPEED)
    }
}
