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
package org.kryptonmc.krypton.entity

import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentSetOf
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TranslatableComponent
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.BlockState
import org.kryptonmc.api.block.Blocks
import org.kryptonmc.api.entity.Entity
import org.kryptonmc.api.entity.EntityCategory
import org.kryptonmc.api.entity.EntityType
import org.kryptonmc.krypton.world.block.downcast
import org.kryptonmc.krypton.world.block.isBurning
import org.kryptonmc.krypton.world.block.state.KryptonBlockState

@JvmRecord
data class KryptonEntityType<out T : Entity>(
    private val key: Key,
    override val category: EntityCategory,
    override val isSummonable: Boolean,
    override val isImmuneToFire: Boolean,
    override val clientTrackingRange: Int,
    override val updateInterval: Int,
    override val width: Float,
    override val height: Float,
    override val immuneTo: ImmutableSet<Block>,
    override val lootTable: Key,
    override val translation: TranslatableComponent
) : EntityType<T> {

    override fun isImmuneTo(block: BlockState): Boolean = isImmuneTo(block.downcast())

    fun isImmuneTo(block: KryptonBlockState): Boolean {
        if (immuneTo.contains(block.block)) return true
        if (!isImmuneToFire && block.isBurning()) return false
        return block.eq(Blocks.WITHER_ROSE) || block.eq(Blocks.SWEET_BERRY_BUSH) || block.eq(Blocks.CACTUS) || block.eq(Blocks.POWDER_SNOW)
    }

    override fun key(): Key = key

    class Builder<T : Entity>(private var key: Key, private var category: EntityCategory) : EntityType.Builder<T> {

        private var summonable = true
        private var fireImmune = false
        private var width = DEFAULT_WIDTH
        private var height = DEFAULT_HEIGHT
        private val immuneTo = persistentSetOf<Block>().builder()
        private var clientTrackingRange = DEFAULT_CLIENT_TRACKING_RANGE
        private var updateInterval = DEFAULT_UPDATE_INTERVAL
        private var lootTable: Key? = null
        private var translation: TranslatableComponent? = null

        override fun key(key: Key): Builder<T> = apply { this.key = key }

        override fun category(category: EntityCategory): Builder<T> = apply { this.category = category }

        override fun summonable(value: Boolean): Builder<T> = apply { summonable = value }

        override fun fireImmune(value: Boolean): Builder<T> = apply { fireImmune = value }

        override fun clientTrackingRange(range: Int): Builder<T> = apply { clientTrackingRange = range }

        override fun updateInterval(interval: Int): Builder<T> = apply { updateInterval = interval }

        override fun width(width: Float): Builder<T> = apply { this.width = width }

        override fun height(height: Float): Builder<T> = apply { this.height = height }

        override fun immuneTo(block: Block): Builder<T> = apply { immuneTo.add(block) }

        override fun immuneTo(vararg blocks: Block): Builder<T> = apply { blocks.forEach(immuneTo::add) }

        override fun immuneTo(blocks: Iterable<Block>): Builder<T> = apply { immuneTo.addAll(blocks) }

        override fun lootTable(identifier: Key): Builder<T> = apply { lootTable = identifier }

        override fun translation(translation: TranslatableComponent): Builder<T> = apply { this.translation = translation }

        override fun build(): KryptonEntityType<T> = KryptonEntityType(
            key,
            category,
            summonable,
            fireImmune,
            clientTrackingRange,
            updateInterval,
            width,
            height,
            immuneTo.build(),
            lootTable ?: Key.key(key.namespace(), "entities/${key.value()}"),
            translation ?: Component.translatable("entity.${key.namespace()}.${key.value().replace('/', '.')}")
        )

        companion object {

            private const val DEFAULT_WIDTH = 0.6F
            private const val DEFAULT_HEIGHT = 1.8F
            private const val DEFAULT_CLIENT_TRACKING_RANGE = 5
            private const val DEFAULT_UPDATE_INTERVAL = 3
        }
    }

    object Factory : EntityType.Factory {

        override fun <T : Entity> builder(key: Key, category: EntityCategory): EntityType.Builder<T> = Builder(key, category)
    }
}
