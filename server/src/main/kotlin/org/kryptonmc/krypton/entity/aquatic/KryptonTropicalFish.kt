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
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.item.meta.KryptonItemMeta
import org.kryptonmc.krypton.world.KryptonWorld
import kotlin.math.min

class KryptonTropicalFish(world: KryptonWorld) : KryptonSchoolingFish(world, EntityTypes.TROPICAL_FISH), TropicalFish {

    override var baseColor: DyeColor
        get() = extractBaseColor(data[MetadataKeys.TROPICAL_FISH.VARIANT])
        set(value) {
            val variant = data[MetadataKeys.TROPICAL_FISH.VARIANT]
            data[MetadataKeys.TROPICAL_FISH.VARIANT] = modifyBaseColor(variant, value)
        }
    override var patternColor: DyeColor
        get() = extractPatternColor(data[MetadataKeys.TROPICAL_FISH.VARIANT])
        set(value) {
            val variant = data[MetadataKeys.TROPICAL_FISH.VARIANT]
            data[MetadataKeys.TROPICAL_FISH.VARIANT] = modifyPatternColor(variant, value)
        }
    override var variant: TropicalFishVariant
        get() = extractVariant(data[MetadataKeys.TROPICAL_FISH.VARIANT])
        set(value) {
            val variant = data[MetadataKeys.TROPICAL_FISH.VARIANT]
            data[MetadataKeys.TROPICAL_FISH.VARIANT] = modifyVariant(variant, value)
        }

    override val bucketType: ItemType = ItemTypes.TROPICAL_FISH_BUCKET

    init {
        data.add(MetadataKeys.TROPICAL_FISH.VARIANT)
    }

    /* FIXME
    override fun saveToBucket(item: KryptonItemStack) {
        item.meta.nbt = item.meta.nbt.putInt("BucketVariantTag", data[MetadataKeys.TROPICAL_FISH.VARIANT])
    }
     */

    companion object {

        private val VARIANTS = TropicalFishVariant.values()

        /**
         * Tropical fish data is all packed in to a single 32-bit integer, with 8 bits being
         * used to represent each part that combine to make the variant.
         *
         * The encoding is better visualised with a table:
         * | Pattern colour | Base colour | Pattern | Shape  |
         * | -------------- | ----------- | ------- | ------ |
         * | 8 bits         | 8 bits      | 8 bits  | 8 bits |
         *
         * For example, a variant with shape 1, pattern 4, base colour gray, and pattern colour
         * purple would have the following encoded variant:
         * | Pattern colour | Base colour | Pattern  | Shape    |
         * | -------------- | ----------- | -------- | -------- |
         * | 00001010       | 00000111    | 00000100 | 00000001 |
         * Combining together in to 00001010000001110000010000000001
         *
         * For the modifications, we use AND mechanics to clear the bits we want to replace.
         * Because of the way AND works, the following are always true, where a is any binary digit:
         * * a AND 1 = a
         * * a AND 0 = 0
         */
        @JvmStatic
        private fun encodeVariant(variant: TropicalFishVariant, baseColor: DyeColor, patternColor: DyeColor): Int {
            val baseId = Registries.DYE_COLORS.idOf(baseColor)
            val patternId = Registries.DYE_COLORS.idOf(patternColor)
            return variant.shape and 255 or ((variant.pattern and 255) shl 8) or ((baseId and 255) shl 16) or ((patternId and 255) shl 24)
        }

        // -65536 = 11111111 11111111 00000000 00000000 (clear bits 0-15)
        @JvmStatic
        private fun modifyVariant(encoded: Int, variant: TropicalFishVariant): Int {
            val encodedVariant = (variant.shape and 255) or ((variant.pattern and 255) shl 8)
            return (encoded and -65536) or encodedVariant
        }

        // -16711681 = 11111111 00000000 11111111 11111111 (clear bits 16-23)
        @JvmStatic
        private fun modifyBaseColor(encoded: Int, baseColor: DyeColor): Int {
            val colorId = Registries.DYE_COLORS.idOf(baseColor)
            return (encoded and -16711681) or ((colorId and 255) shl 16)
        }

        // 16777215 = 00000000 11111111 11111111 11111111 (clear bits 24-31)
        @JvmStatic
        private fun modifyPatternColor(encoded: Int, patternColor: DyeColor): Int {
            val colorId = Registries.DYE_COLORS.idOf(patternColor)
            return (encoded and 16777215) or ((colorId and 255) shl 24)
        }

        // 16711680 = 00000000 11111111 00000000 00000000 (clear all bits except 16-23)
        @JvmStatic
        private fun extractBaseColor(variant: Int): DyeColor = Registries.DYE_COLORS[(variant and 16711680) shr 16]!!

        // -16777216 = 11111111 00000000 00000000 00000000 (clear all bits except 24-31)
        @JvmStatic
        private fun extractPatternColor(encoded: Int): DyeColor = Registries.DYE_COLORS[(encoded and -16777216) shr 24]!!

        // 255 = 00000000 00000000 00000000 11111111 (clear all bits except 0-7)
        // 65280 = 00000000 00000000 11111111 00000000 (clear all bits except 8-15)
        @JvmStatic
        private fun extractVariant(encoded: Int): TropicalFishVariant = VARIANTS[min(encoded and 255, 1) + 6 * min((encoded and 65280) shr 8, 5)]
    }
}
