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
package org.kryptonmc.krypton.item.data

import com.google.common.collect.ImmutableList
import org.kryptonmc.api.item.data.FireworkEffect
import org.kryptonmc.api.item.data.FireworkEffectType
import org.kryptonmc.api.util.Color
import org.kryptonmc.krypton.util.collection.ListExtras
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.compound
import java.util.Arrays

@JvmRecord
data class KryptonFireworkEffect(
    override val type: FireworkEffectType,
    override val hasFlicker: Boolean,
    override val hasTrail: Boolean,
    override val colors: ImmutableList<Color>,
    override val fadeColors: ImmutableList<Color>
) : FireworkEffect {

    class Builder(private val type: FireworkEffectType) : FireworkEffect.Builder {

        private var flicker = false
        private var trail = false
        private var colors = ImmutableList.of<Color>()
        private var fadeColors = ImmutableList.of<Color>()

        override fun flickers(): Builder = apply { flicker = true }

        override fun trail(): Builder = apply { trail = true }

        override fun colors(colors: Collection<Color>): Builder = apply { this.colors = ImmutableList.copyOf(colors) }

        override fun fadeColors(colors: Collection<Color>): Builder = apply { fadeColors = ImmutableList.copyOf(colors) }

        override fun build(): KryptonFireworkEffect = KryptonFireworkEffect(type, flicker, trail, colors, fadeColors)
    }

    object Factory : FireworkEffect.Factory {

        override fun builder(type: FireworkEffectType): Builder = Builder(type)
    }

    companion object {

        private const val TYPE_TAG = "Type"
        private const val FLICKER_TAG = "Flicker"
        private const val TRAIL_TAG = "Trail"
        private const val COLORS_TAG = "Colors"
        private const val FADE_COLORS_TAG = "FadeColors"
        private val EFFECT_TYPES = FireworkEffectType.values()

        @JvmStatic
        fun load(data: CompoundTag): KryptonFireworkEffect {
            val type = EFFECT_TYPES[data.getByte(TYPE_TAG).toInt()]
            val colors = getColors(data, COLORS_TAG)
            val fadeColors = getColors(data, FADE_COLORS_TAG)
            return KryptonFireworkEffect(type, data.getBoolean(FLICKER_TAG), data.getBoolean(TRAIL_TAG), colors, fadeColors)
        }

        @JvmStatic
        fun save(effect: FireworkEffect): CompoundTag = compound {
            putByte(TYPE_TAG, effect.type.ordinal.toByte())
            putBoolean(FLICKER_TAG, effect.hasFlicker)
            putBoolean(TRAIL_TAG, effect.hasTrail)
            putIntArray(COLORS_TAG, ListExtras.toIntArray(effect.colors, Color::value))
            putIntArray(FADE_COLORS_TAG, ListExtras.toIntArray(effect.fadeColors, Color::value))
        }

        @JvmStatic
        private fun getColors(data: CompoundTag, name: String): ImmutableList<Color> =
            Arrays.stream(data.getIntArray(name)).mapToObj { Color.of(it) }.collect(ImmutableList.toImmutableList())
    }
}
