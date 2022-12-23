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

import org.kryptonmc.api.entity.aquatic.TropicalFish
import org.kryptonmc.api.entity.aquatic.TropicalFishVariant
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.api.item.data.DyeColor
import org.kryptonmc.krypton.entity.KryptonEntityType
import org.kryptonmc.krypton.entity.KryptonEntityTypes
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.serializer.aquatic.TropicalFishSerializer
import org.kryptonmc.krypton.util.DyeColors
import org.kryptonmc.krypton.world.KryptonWorld
import kotlin.math.min

class KryptonTropicalFish(world: KryptonWorld) : KryptonSchoolingFish(world), TropicalFish {

    override val type: KryptonEntityType<KryptonTropicalFish>
        get() = KryptonEntityTypes.TROPICAL_FISH
    override val serializer: EntitySerializer<KryptonTropicalFish>
        get() = TropicalFishSerializer
    override val bucketType: ItemType
        get() = ItemTypes.TROPICAL_FISH_BUCKET.get()

    override var baseColor: DyeColor
        get() = extractBaseColor(data.get(MetadataKeys.TropicalFish.VARIANT))
        set(value) = updateVariant(::modifyBaseColor, value)
    override var patternColor: DyeColor
        get() = extractPatternColor(data.get(MetadataKeys.TropicalFish.VARIANT))
        set(value) = updateVariant(::modifyPatternColor, value)
    override var variant: TropicalFishVariant
        get() = extractVariant(data.get(MetadataKeys.TropicalFish.VARIANT))
        set(value) = updateVariant(::modifyVariant, value)

    override fun defineData() {
        super.defineData()
        data.define(MetadataKeys.TropicalFish.VARIANT, TropicalFishVariant.KOB.ordinal)
    }

    /* FIXME
    override fun saveToBucket(item: KryptonItemStack) {
        item.meta.nbt = item.meta.nbt.putInt("BucketVariantTag", data.get(MetadataKeys.TROPICAL_FISH.VARIANT))
    }
     */

    private inline fun <T> updateVariant(modifier: (Int, T) -> Int, value: T) {
        data.set(MetadataKeys.TropicalFish.VARIANT, modifier(data.get(MetadataKeys.TropicalFish.VARIANT), value))
    }

    @Suppress("MagicNumber") // Nothing in here is magic as it's all explained in comments
    companion object {

        private val VARIANTS = TropicalFishVariant.values()
        private const val NO_SHAPE_PATTERN_MASK = -65536 // 11111111 11111111 00000000 00000000 (clear bits 0-15)
        private const val NO_BASE_COLOR_MASK = -16711681 // 11111111 00000000 11111111 11111111 (clear bits 16-23)
        private const val NO_PATTERN_COLOR_MASK = -65281 // 11111111 11111111 00000000 11111111 (clear bits 8-15)
        private const val SHAPE_MASK = 255 // 00000000 00000000 00000000 11111111 (clear all bits except 0-7)
        private const val PATTERN_MASK = 65280 // 00000000 00000000 11111111 00000000 (clear all bits except 8-15)
        private const val BASE_COLOR_MASK = 16711680 // 00000000 11111111 00000000 00000000 (clear all bits except 16-23)
        private const val PATTERN_COLOR_MASK = -16777216 // 11111111 00000000 00000000 00000000 (clear all bits except 24-31)

        /*
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
         * - a AND 1 = a
         * - a AND 0 = 0
         */

        /*
         * The + 2 shr 3 here is a trick to get a 0 for values 5 and under and a 1 for values 6 and over (up to 13, though we only have 12 values)
         * Because 7 is the maximum value you can fit in 3 bits, anything above 7 up to 15 will always have the 8 bit (bit 3) set to 1, and as it's
         * 5 and up, we add 2 to make it up to 7. Therefore, every value 7 and below will have bit 3 set to 0, and every value 8-15 will have bit 3
         * set to 1. Therefore, we add 2 to make it up to 7 instead of up to 5, and then shift right by 3 to get the value of bit 3.
         *
         * Huge micro-optimisation for this sort of thing, but it does make for some very clean code.
         *
         * Also, for the second part, doing mod 6 produces a number from 0-5 for indices up to 5 and then 0-5 for indices up to 11. Essentially,
         * it's a way to reset the value back to 0 after 6 values.
         */
        @JvmStatic
        private fun modifyVariant(encoded: Int, variant: TropicalFishVariant): Int =
            encoded and NO_SHAPE_PATTERN_MASK or (variant.ordinal + 2 shr 3 or (variant.ordinal % 6 shl 8))

        @JvmStatic
        private fun modifyBaseColor(encoded: Int, baseColor: DyeColor): Int = encoded and NO_BASE_COLOR_MASK or (baseColor.ordinal shl 16)

        @JvmStatic
        private fun modifyPatternColor(encoded: Int, patternColor: DyeColor): Int =
            encoded and NO_PATTERN_COLOR_MASK or (patternColor.ordinal shl 24)

        @JvmStatic
        private fun extractBaseColor(variant: Int): DyeColor = DyeColors.fromId(variant and BASE_COLOR_MASK shr 16)

        @JvmStatic
        private fun extractPatternColor(encoded: Int): DyeColor = DyeColors.fromId(encoded and PATTERN_COLOR_MASK shr 24)

        @JvmStatic
        private fun extractVariant(encoded: Int): TropicalFishVariant =
            VARIANTS[min(encoded and SHAPE_MASK, 1) + 6 * min(encoded and PATTERN_MASK shr 8, 5)]
    }
}
