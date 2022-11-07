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

import com.google.common.collect.ImmutableSet
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.BlockState
import org.kryptonmc.api.block.Blocks
import org.kryptonmc.api.entity.EntityCategory
import org.kryptonmc.api.entity.EntityType
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.util.Keys
import org.kryptonmc.krypton.world.block.downcast
import org.kryptonmc.krypton.world.block.isBurning
import org.kryptonmc.krypton.world.block.state.KryptonBlockState
import javax.annotation.concurrent.Immutable

@Immutable
class KryptonEntityType<out T : KryptonEntity>(
    override val category: EntityCategory,
    val isSerializable: Boolean,
    override val isSummonable: Boolean,
    override val isImmuneToFire: Boolean,
    val canSpawnFarFromPlayer: Boolean,
    override val immuneTo: ImmutableSet<Block>,
    override val width: Float,
    override val height: Float,
    override val clientTrackingRange: Int,
    override val updateInterval: Int
) : EntityType<T> {

    private var descriptionId: String? = null
    private var description: Component? = null
    private var cachedLootTable: Key? = null

    override val lootTable: Key
        get() {
            if (cachedLootTable == null) {
                val key = key()
                cachedLootTable = Key.key(key.namespace(), "entities/${key.value()}")
            }
            return cachedLootTable!!
        }

    override fun isImmuneTo(block: BlockState): Boolean = isImmuneTo(block.downcast())

    fun isImmuneTo(block: KryptonBlockState): Boolean {
        if (immuneTo.contains(block.block)) return true
        if (!isImmuneToFire && block.isBurning()) return false
        return block.eq(Blocks.WITHER_ROSE) || block.eq(Blocks.SWEET_BERRY_BUSH) || block.eq(Blocks.CACTUS) || block.eq(Blocks.POWDER_SNOW)
    }

    override fun key(): Key = Registries.ENTITY_TYPE.get(this)

    override fun translationKey(): String {
        if (descriptionId == null) descriptionId = Keys.translation("entity", key())
        return descriptionId!!
    }

    fun description(): Component {
        if (description == null) description = Component.translatable(translationKey())
        return description!!
    }

    class Builder<out T : KryptonEntity>(private val category: EntityCategory) {

        private var immuneTo = ImmutableSet.of<Block>()
        private var serializable = true
        private var summonable = true
        private var fireImmune = false
        private var canSpawnFarFromPlayer = false
        private var clientTrackingRange = 5
        private var updateInterval = 3
        private var width = 0.6F
        private var height = 1.8F

        fun size(width: Float, height: Float): Builder<T> = apply {
            this.width = width
            this.height = height
        }

        fun notSummonable(): Builder<T> = apply { summonable = false }

        fun notSerializable(): Builder<T> = apply { serializable = false }

        fun fireImmune(): Builder<T> = apply { fireImmune = true }

        fun immuneTo(vararg blocks: Block): Builder<T> = apply { immuneTo = ImmutableSet.copyOf(blocks) }

        fun canSpawnFarFromPlayer(): Builder<T> = apply { canSpawnFarFromPlayer = true }

        fun clientTrackingRange(range: Int): Builder<T> = apply { clientTrackingRange = range }

        fun updateInterval(interval: Int): Builder<T> = apply { updateInterval = interval }

        fun build(): KryptonEntityType<T> = KryptonEntityType(
            category,
            serializable,
            summonable,
            fireImmune,
            canSpawnFarFromPlayer,
            immuneTo,
            width,
            height,
            clientTrackingRange,
            updateInterval
        )
    }
}
