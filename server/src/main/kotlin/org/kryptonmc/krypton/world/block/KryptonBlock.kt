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
package org.kryptonmc.krypton.world.block

import com.github.benmanes.caffeine.cache.Caffeine
import net.kyori.adventure.key.Key
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.BlockSoundGroup
import org.kryptonmc.api.block.BlockState
import org.kryptonmc.api.util.Direction
import org.kryptonmc.api.util.Vec3i
import org.kryptonmc.internal.annotations.CataloguedBy
import org.kryptonmc.krypton.item.KryptonItemType
import org.kryptonmc.krypton.registry.holder.Holder
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.shapes.util.BooleanOperator
import org.kryptonmc.krypton.shapes.Shapes
import org.kryptonmc.krypton.shapes.VoxelShape
import org.kryptonmc.krypton.state.StateDefinition
import org.kryptonmc.krypton.state.StateHolderDelegate
import org.kryptonmc.krypton.state.property.KryptonProperty
import org.kryptonmc.krypton.util.map.IntHashBiMap
import org.kryptonmc.krypton.util.Keys
import org.kryptonmc.krypton.world.components.WorldAccessor
import org.kryptonmc.krypton.world.block.handler.BlockHandler
import org.kryptonmc.krypton.world.block.handler.BlockPropertiesProvider
import org.kryptonmc.krypton.world.block.handler.RedstoneDataProvider
import org.kryptonmc.krypton.world.block.handler.BlockShapesProvider
import org.kryptonmc.krypton.world.block.state.KryptonBlockState
import org.kryptonmc.krypton.world.chunk.flag.SetBlockFlag

@Suppress("LeakingThis")
@CataloguedBy(KryptonBlocks::class)
class KryptonBlock(
    val properties: BlockProperties,
    val handler: BlockHandler,
    val propertiesProvider: BlockPropertiesProvider,
    val redstoneDataProvider: RedstoneDataProvider,
    val shapesProvider: BlockShapesProvider,
    stateProperties: Collection<KryptonProperty<*>>
) : Block, StateHolderDelegate<BlockState, KryptonBlockState> {

    override val stateDefinition: StateDefinition<KryptonBlock, KryptonBlockState>

    val builtInRegistryHolder: Holder.Reference<KryptonBlock> = KryptonRegistries.BLOCK.createIntrusiveHolder(this)

    override val explosionResistance: Double
        get() = properties.explosionResistance.toDouble()
    override val soundGroup: BlockSoundGroup
        get() = properties.soundGroup
    override val friction: Double
        get() = properties.friction.toDouble()

    override val defaultState: KryptonBlockState
        get() = defaultBlockState

    private val defaultBlockState: KryptonBlockState
    private var descriptionId: String? = null
    private var item: KryptonItemType? = null
    private var drops: Key? = null

    init {
        val builder = StateDefinition.Builder<KryptonBlock, KryptonBlockState>(this)
        builder.add(stateProperties)
        stateDefinition = builder.build({ it.defaultBlockState }, ::KryptonBlockState)
        defaultBlockState = stateDefinition.any()
    }

    override fun hasGravity(): Boolean = propertiesProvider.hasGravity()

    override fun hasBlockEntity(): Boolean = propertiesProvider.hasBlockEntity()

    override fun canRespawnIn(): Boolean = !properties.material.solid && !properties.material.liquid

    override fun asItem(): KryptonItemType {
        if (item == null) item = KryptonItemType.fromBlock(this)
        return item!!
    }

    override fun asBlock(): KryptonBlock = this

    override fun key(): Key = KryptonRegistries.BLOCK.getKey(this)

    override fun translationKey(): String {
        if (descriptionId == null) descriptionId = Keys.translation("block", key())
        return descriptionId!!
    }

    fun lootTable(): Key {
        if (drops == null) {
            val registryKey = requireNotNull(KryptonRegistries.BLOCK.getKey(this)) { "Could not find registry key for block $this!" }
            drops = Key.key(registryKey.namespace(), "blocks/${registryKey.value()}")
        }
        return drops!!
    }

    override fun toString(): String = "KryptonBlock(${key()})"

    companion object {

        private const val BOX_FACTOR = 16.0
        @JvmField
        val STATES: IntHashBiMap<KryptonBlockState> = IntHashBiMap()
        private val SHAPE_FULL_BLOCK_CACHE = Caffeine.newBuilder()
            .maximumSize(512)
            .weakKeys()
            .build<VoxelShape, Boolean> { !Shapes.joinIsNotEmpty(Shapes.block(), it, BooleanOperator.NOT_SAME) }

        @JvmStatic
        fun stateFromId(id: Int): KryptonBlockState = STATES.get(id) ?: KryptonBlocks.AIR.defaultState

        @JvmStatic
        fun idOf(state: KryptonBlockState?): Int {
            if (state == null) return 0
            val id = STATES.getId(state)
            return if (id == -1) 0 else id
        }

        @JvmStatic
        fun box(minX: Double, minY: Double, minZ: Double, maxX: Double, maxY: Double, maxZ: Double): VoxelShape =
            Shapes.box(minX / BOX_FACTOR, minY / BOX_FACTOR, minZ / BOX_FACTOR, maxX / BOX_FACTOR, maxY / BOX_FACTOR, maxZ / BOX_FACTOR)

        @JvmStatic
        fun isFaceFull(shape: VoxelShape, face: Direction): Boolean = isShapeFullBlock(shape.getFaceShape(face))

        @JvmStatic
        fun isShapeFullBlock(shape: VoxelShape): Boolean = SHAPE_FULL_BLOCK_CACHE.get(shape)

        @JvmStatic
        fun updateOrDestroy(oldState: KryptonBlockState, newState: KryptonBlockState, world: WorldAccessor, pos: Vec3i, flags: Int,
                            recursionLeft: Int) {
            if (oldState === newState) return
            if (oldState.isAir()) {
                world.destroyBlock(pos, flags and SetBlockFlag.NEIGHBOUR_DROPS == 0, null, recursionLeft)
            } else {
                world.setBlock(pos, newState, flags and SetBlockFlag.NO_NEIGHBOUR_DROPS, recursionLeft)
            }
        }
    }
}
