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
package org.kryptonmc.krypton.item

import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TranslatableComponent
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.effect.sound.SoundEvents
import org.kryptonmc.api.item.ItemRarities
import org.kryptonmc.api.item.ItemRarity
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.util.normalizePath

@JvmRecord
data class KryptonItemType(
    private val key: Key,
    override val rarity: ItemRarity,
    override val maximumStackSize: Int,
    override val canBreak: Boolean,
    override val durability: Int,
    override val isEdible: Boolean,
    override val isFireResistant: Boolean,
    override val eatingSound: SoundEvent,
    override val drinkingSound: SoundEvent,
    override val translation: TranslatableComponent
) : ItemType {

    override fun key(): Key = key

    override fun asBlock(): Block? = Registries.BLOCK[key]

    override fun toBuilder(): ItemType.Builder = Builder(this)

    class Builder(private var key: Key) : ItemType.Builder {

        private var rarity = ItemRarities.COMMON
        private var maximumStackSize = 64
        private var canBreak = false
        private var durability = 0
        private var isEdible = false
        private var isFireResistant = false
        private var eatingSound = SoundEvents.GENERIC_EAT
        private var drinkingSound = SoundEvents.GENERIC_DRINK
        private var translation: TranslatableComponent? = null

        constructor(type: ItemType) : this(type.key()) {
            rarity = type.rarity
            maximumStackSize = type.maximumStackSize
            canBreak = type.canBreak
            durability = type.durability
            isEdible = type.isEdible
            isFireResistant = type.isFireResistant
            eatingSound = type.eatingSound
            drinkingSound = type.drinkingSound
        }

        override fun key(key: Key): ItemType.Builder = apply { this.key = key }

        override fun rarity(rarity: ItemRarity): ItemType.Builder = apply { this.rarity = rarity }

        override fun stackable(maximumStackSize: Int): ItemType.Builder = apply { this.maximumStackSize = maximumStackSize }

        override fun unstackable(): ItemType.Builder = apply { maximumStackSize = 1 }

        override fun breakable(): ItemType.Builder = apply { canBreak = true }

        override fun breakable(durability: Int): ItemType.Builder = apply {
            canBreak = true
            this.durability = durability
        }

        override fun unbreakable(): ItemType.Builder = apply {
            canBreak = false
            durability = 0
        }

        override fun durability(durability: Int): ItemType.Builder = apply { this.durability = durability }

        override fun edible(): ItemType.Builder = apply { isEdible = true }

        override fun inedible(): ItemType.Builder = apply { isEdible = false }

        override fun fireResistant(): ItemType.Builder = apply { isFireResistant = true }

        override fun flammable(): ItemType.Builder = apply { isFireResistant = false }

        override fun eatingSound(eatingSound: SoundEvent): ItemType.Builder = apply { this.eatingSound = eatingSound }

        override fun drinkingSound(drinkingSound: SoundEvent): ItemType.Builder = apply { this.drinkingSound = drinkingSound }

        override fun translation(translation: TranslatableComponent): ItemType.Builder = apply { this.translation = translation }

        override fun build(): ItemType = KryptonItemType(
            key,
            rarity,
            maximumStackSize,
            canBreak,
            durability,
            isEdible,
            isFireResistant,
            eatingSound,
            drinkingSound,
            translation ?: createTranslation(key)
        )
    }

    object Factory : ItemType.Factory {

        override fun builder(key: Key): ItemType.Builder = Builder(key)
    }

    companion object {

        @JvmStatic
        private fun createTranslation(key: Key): TranslatableComponent {
            val block = Registries.BLOCK[key]
            if (block != null) return block.translation
            return Component.translatable("item.${key.normalizePath()}")
        }
    }
}
