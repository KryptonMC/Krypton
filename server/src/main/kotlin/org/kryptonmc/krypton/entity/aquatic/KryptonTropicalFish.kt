/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.entity.aquatic

import org.kryptonmc.api.entity.EntityTypes
import org.kryptonmc.api.entity.aquatic.TropicalFish
import org.kryptonmc.api.entity.aquatic.TropicalFishVariant
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.api.item.data.DyeColor
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.world.KryptonWorld

class KryptonTropicalFish(world: KryptonWorld) : KryptonSchoolingFish(world, EntityTypes.TROPICAL_FISH), TropicalFish {

    override var baseColor: DyeColor
        get() = TropicalFishVariants.extractBaseColor(data[MetadataKeys.TROPICAL_FISH.VARIANT])
        set(value) {
            val variant = data[MetadataKeys.TROPICAL_FISH.VARIANT]
            data[MetadataKeys.TROPICAL_FISH.VARIANT] = TropicalFishVariants.modifyBaseColor(variant, value)
        }
    override var patternColor: DyeColor
        get() = TropicalFishVariants.extractPatternColor(data[MetadataKeys.TROPICAL_FISH.VARIANT])
        set(value) {
            val variant = data[MetadataKeys.TROPICAL_FISH.VARIANT]
            data[MetadataKeys.TROPICAL_FISH.VARIANT] = TropicalFishVariants.modifyPatternColor(variant, value)
        }
    override var variant: TropicalFishVariant
        get() = TropicalFishVariants.extractVariant(data[MetadataKeys.TROPICAL_FISH.VARIANT])
        set(value) {
            val variant = data[MetadataKeys.TROPICAL_FISH.VARIANT]
            data[MetadataKeys.TROPICAL_FISH.VARIANT] = TropicalFishVariants.modifyVariant(variant, value)
        }

    override val bucketType: ItemType = ItemTypes.TROPICAL_FISH_BUCKET

    init {
        data.add(MetadataKeys.TROPICAL_FISH.VARIANT, TropicalFishVariant.KOB.ordinal)
    }

    /* FIXME
    override fun saveToBucket(item: KryptonItemStack) {
        item.meta.nbt = item.meta.nbt.putInt("BucketVariantTag", data[MetadataKeys.TROPICAL_FISH.VARIANT])
    }
     */
}
