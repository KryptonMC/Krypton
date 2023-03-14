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
package org.kryptonmc.krypton.entity

import com.google.common.collect.ImmutableSet
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import org.kryptonmc.api.block.BlockState
import org.kryptonmc.api.entity.EntityCategory
import org.kryptonmc.api.entity.EntityType
import org.kryptonmc.api.tags.TagKey
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.util.Keys
import org.kryptonmc.krypton.world.block.KryptonBlock
import org.kryptonmc.krypton.world.block.KryptonBlocks
import org.kryptonmc.krypton.world.block.isBurning
import org.kryptonmc.krypton.world.block.state.KryptonBlockState
import org.kryptonmc.krypton.world.block.state.downcast
import javax.annotation.concurrent.Immutable

@Immutable
class KryptonEntityType<out T : KryptonEntity>(
    override val category: EntityCategory,
    val isSerializable: Boolean,
    override val isSummonable: Boolean,
    override val isImmuneToFire: Boolean,
    val canSpawnFarFromPlayer: Boolean,
    override val immuneTo: ImmutableSet<KryptonBlock>,
    override val width: Float,
    override val height: Float,
    override val clientTrackingRange: Int,
    override val updateInterval: Int
) : EntityType<T> {

    private var descriptionId: String? = null
    private var description: Component? = null
    private var cachedLootTable: Key? = null

    private val builtInRegistryHolder = KryptonRegistries.ENTITY_TYPE.createIntrusiveHolder(this)
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
        // This is vanilla logic
        if (immuneTo.contains(block.block)) return true
        if (!isImmuneToFire && block.isBurning()) return false
        return block.eq(KryptonBlocks.WITHER_ROSE) || block.eq(KryptonBlocks.SWEET_BERRY_BUSH) || block.eq(KryptonBlocks.CACTUS) ||
                block.eq(KryptonBlocks.POWDER_SNOW)
    }

    override fun key(): Key = KryptonRegistries.ENTITY_TYPE.getKey(this)

    override fun translationKey(): String {
        if (descriptionId == null) descriptionId = Keys.translation("entity", key())
        return descriptionId!!
    }

    fun description(): Component {
        if (description == null) description = Component.translatable(translationKey())
        return description!!
    }

    @Suppress("UNCHECKED_CAST")
    fun eq(tag: TagKey<EntityType<*>>): Boolean = builtInRegistryHolder.eq(tag as TagKey<KryptonEntityType<*>>)

    class Builder<out T : KryptonEntity>(private val category: EntityCategory) {

        private var immuneTo = ImmutableSet.of<KryptonBlock>()
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

        fun immuneTo(vararg blocks: KryptonBlock): Builder<T> = apply { immuneTo = ImmutableSet.copyOf(blocks) }

        fun canSpawnFarFromPlayer(): Builder<T> = apply { canSpawnFarFromPlayer = true }

        fun clientTrackingRange(range: Int): Builder<T> = apply { clientTrackingRange = range }

        fun updateInterval(interval: Int): Builder<T> = apply { updateInterval = interval }

        fun build(): KryptonEntityType<T> {
            return KryptonEntityType(category, serializable, summonable, fireImmune, canSpawnFarFromPlayer, immuneTo, width, height,
                clientTrackingRange, updateInterval)
        }
    }
}
