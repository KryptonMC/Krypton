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
package org.kryptonmc.krypton.entity

import com.google.common.collect.ImmutableSet
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TranslatableComponent
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.Blocks
import org.kryptonmc.api.entity.Entity
import org.kryptonmc.api.entity.EntityCategory
import org.kryptonmc.api.entity.EntityDimensions
import org.kryptonmc.api.entity.EntityType
import org.kryptonmc.krypton.world.block.isBurning

@JvmRecord
data class KryptonEntityType<T : Entity>(
    private val key: Key,
    override val category: EntityCategory,
    override val isSummonable: Boolean,
    override val isImmuneToFire: Boolean,
    override val clientTrackingRange: Int,
    override val updateInterval: Int,
    override val dimensions: EntityDimensions,
    override val immuneTo: Set<Block>,
    override val lootTable: Key,
    override val translation: TranslatableComponent
) : EntityType<T> {

    override fun isImmuneTo(block: Block): Boolean {
        if (immuneTo.contains(block)) return true
        if (!isImmuneToFire && block.isBurning()) return false
        return block !== Blocks.WITHER_ROSE && block !== Blocks.SWEET_BERRY_BUSH && block !== Blocks.CACTUS && block !== Blocks.POWDER_SNOW
    }

    override fun key(): Key = key

    class Builder<T : Entity>(private val key: Key, private var category: EntityCategory) : EntityType.Builder<T> {

        private var summonable = true
        private var fireImmune = false
        private var dimensions = DEFAULT_DIMENSIONS
        private val immuneTo = mutableSetOf<Block>()
        private var clientTrackingRange = 5
        private var updateInterval = 3
        private var lootTable: Key? = null
        private var translation: TranslatableComponent? = null

        constructor(type: EntityType<T>) : this(type.key(), type.category) {
            summonable = type.isSummonable
            fireImmune = type.isImmuneToFire
            dimensions = type.dimensions
            immuneTo.addAll(type.immuneTo)
            lootTable = type.lootTable
        }

        override fun category(category: EntityCategory): EntityType.Builder<T> = apply { this.category = category }

        override fun summonable(value: Boolean): EntityType.Builder<T> = apply { summonable = value }

        override fun fireImmune(value: Boolean): EntityType.Builder<T> = apply { fireImmune = value }

        override fun clientTrackingRange(range: Int): EntityType.Builder<T> = apply { clientTrackingRange = range }

        override fun updateInterval(interval: Int): EntityType.Builder<T> = apply { updateInterval = interval }

        override fun dimensions(dimensions: EntityDimensions): EntityType.Builder<T> = apply { this.dimensions = dimensions }

        override fun immuneTo(block: Block): EntityType.Builder<T> = apply { immuneTo.add(block) }

        override fun immuneTo(vararg blocks: Block): EntityType.Builder<T> = apply { blocks.forEach { immuneTo.add(it) } }

        override fun immuneTo(blocks: Iterable<Block>): EntityType.Builder<T> = apply { immuneTo.addAll(blocks) }

        override fun lootTable(identifier: Key): EntityType.Builder<T> = apply { lootTable = identifier }

        override fun translation(translation: TranslatableComponent): EntityType.Builder<T> = apply { this.translation = translation }

        override fun build() = KryptonEntityType<T>(
            key,
            category,
            summonable,
            fireImmune,
            clientTrackingRange,
            updateInterval,
            dimensions,
            ImmutableSet.copyOf(immuneTo),
            lootTable ?: Key.key(key.namespace(), "entities/${key.value()}"),
            translation ?: Component.translatable("entity.${key.namespace()}.${key.value().replace('/', '.')}")
        )

        companion object {

            private val DEFAULT_DIMENSIONS = EntityDimensions.scalable(0.6F, 1.8F)
        }
    }

    object Factory : EntityType.Factory {

        override fun <T : Entity> of(
            key: Key,
            category: EntityCategory,
            summonable: Boolean,
            fireImmune: Boolean,
            trackingRange: Int,
            updateInterval: Int,
            dimensions: EntityDimensions,
            immuneTo: Set<Block>,
            lootTable: Key,
            translation: TranslatableComponent
        ): EntityType<T> = KryptonEntityType(
            key,
            category,
            summonable,
            fireImmune,
            trackingRange,
            updateInterval,
            dimensions,
            immuneTo,
            lootTable,
            translation
        )

        override fun <T : Entity> builder(key: Key, category: EntityCategory): EntityType.Builder<T> = Builder(key, category)
    }
}
