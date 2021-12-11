/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
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
import org.kryptonmc.api.entity.aquatic.TropicalFishShape
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.api.item.meta.DyeColor
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.nbt.CompoundTag
import kotlin.math.min

class KryptonTropicalFish(world: KryptonWorld) : KryptonSchoolingFish(world, EntityTypes.TROPICAL_FISH), TropicalFish {

    override var baseColor: DyeColor
        get() = extractBaseColor(data[MetadataKeys.TROPICAL_FISH.VARIANT])
        set(value) {
            val variant = data[MetadataKeys.TROPICAL_FISH.VARIANT]
            data[MetadataKeys.TROPICAL_FISH.VARIANT] = calculateVariant(extractType(variant), value, extractPatternColor(variant))
        }
    override var patternColor: DyeColor
        get() = extractPatternColor(data[MetadataKeys.TROPICAL_FISH.VARIANT])
        set(value) {
            val variant = data[MetadataKeys.TROPICAL_FISH.VARIANT]
            data[MetadataKeys.TROPICAL_FISH.VARIANT] = calculateVariant(extractType(variant), extractBaseColor(variant), value)
        }
    override var shape: TropicalFishShape
        get() = extractType(data[MetadataKeys.TROPICAL_FISH.VARIANT])
        set(value) {
            val variant = data[MetadataKeys.TROPICAL_FISH.VARIANT]
            data[MetadataKeys.TROPICAL_FISH.VARIANT] = calculateVariant(value, extractBaseColor(variant), extractPatternColor(variant))
        }

    override val bucketItem: KryptonItemStack
        get() = KryptonItemStack(ItemTypes.TROPICAL_FISH_BUCKET, 1)

    init {
        data.add(MetadataKeys.TROPICAL_FISH.VARIANT)
    }

    override fun load(tag: CompoundTag) {
        super.load(tag)
        data[MetadataKeys.TROPICAL_FISH.VARIANT] = tag.getInt("Variant")
    }

    override fun save(): CompoundTag.Builder = super.save().apply {
        int("Variant", data[MetadataKeys.TROPICAL_FISH.VARIANT])
    }

    override fun saveToBucket(item: KryptonItemStack) {
        item.meta.nbt.putInt("BucketVariantTag", data[MetadataKeys.TROPICAL_FISH.VARIANT])
    }

    companion object {

        @JvmStatic
        private fun calculateVariant(shape: TropicalFishShape, baseColor: DyeColor, patternColor: DyeColor): Int {
            val baseId = Registries.DYE_COLORS.idOf(baseColor)
            val patternId = Registries.DYE_COLORS.idOf(patternColor)
            return shape.category and 255 or ((shape.index and 255) shl 8) or ((baseId and 255) shl 16) or ((patternId and 255) shl 24)
        }

        @JvmStatic
        private fun extractBaseColor(variant: Int): DyeColor = Registries.DYE_COLORS[(variant and 16711680) shr 16]!!

        @JvmStatic
        private fun extractPatternColor(variant: Int): DyeColor = Registries.DYE_COLORS[(variant and -16777216) shr 24]!!

        @JvmStatic
        private fun extractSize(variant: Int): Int = min(variant and 255, 1)

        @JvmStatic
        private fun extractPattern(variant: Int): Int = min(variant and '\uff00'.code shr 8, 5)

        @JvmStatic
        private fun extractType(variant: Int): TropicalFishShape = TropicalFishShape.fromId(extractSize(variant) + 6 * extractPattern(variant))!!
    }
}
