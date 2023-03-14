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
        private val colors = ImmutableList.builder<Color>()
        private val fadeColors = ImmutableList.builder<Color>()

        override fun flickers(): Builder = apply { flicker = true }

        override fun trail(): Builder = apply { trail = true }

        override fun addColor(color: Color): Builder = apply { colors.add(color) }

        override fun addFadeColor(color: Color): Builder = apply { fadeColors.add(color) }

        override fun build(): KryptonFireworkEffect = KryptonFireworkEffect(type, flicker, trail, colors.build(), fadeColors.build())
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
            putIntArray(COLORS_TAG, ListExtras.toIntArray(effect.colors, Color::encode))
            putIntArray(FADE_COLORS_TAG, ListExtras.toIntArray(effect.fadeColors, Color::encode))
        }

        @JvmStatic
        private fun getColors(data: CompoundTag, name: String): ImmutableList<Color> =
            Arrays.stream(data.getIntArray(name)).mapToObj { Color(it) }.collect(ImmutableList.toImmutableList())
    }
}
