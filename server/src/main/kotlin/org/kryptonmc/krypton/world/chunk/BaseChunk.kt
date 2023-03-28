package org.kryptonmc.krypton.world.chunk

import org.kryptonmc.api.entity.Entity
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.util.Vec3i
import org.kryptonmc.api.world.biome.Biome
import org.kryptonmc.api.world.chunk.Chunk
import org.kryptonmc.krypton.coordinate.ChunkPos
import org.kryptonmc.krypton.entity.tracking.EntityTypeTarget
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.biome.NoiseBiomeSource
import org.kryptonmc.krypton.world.block.state.KryptonBlockState
import org.kryptonmc.krypton.world.components.BlockGetter
import org.kryptonmc.krypton.world.fluid.KryptonFluidState
import java.util.function.Predicate

interface BaseChunk : Chunk, BlockGetter, NoiseBiomeSource {

    override val world: KryptonWorld
    val position: ChunkPos

    override val x: Int
        get() = position.x
    override val z: Int
        get() = position.z

    override val entities: Collection<Entity>
        get() = world.entityTracker.entitiesInChunk(position)
    override val players: Collection<Player>
        get() = world.entityTracker.entitiesInChunkOfType(position, EntityTypeTarget.PLAYERS)

    override fun getBlock(x: Int, y: Int, z: Int): KryptonBlockState

    override fun getBlock(position: Vec3i): KryptonBlockState = getBlock(position.x, position.y, position.z)

    override fun getFluid(x: Int, y: Int, z: Int): KryptonFluidState

    override fun getFluid(position: Vec3i): KryptonFluidState = getFluid(position.x, position.y, position.z)

    override fun getBiome(x: Int, y: Int, z: Int): Biome = getNoiseBiome(x, y, z)

    override fun getBiome(position: Vec3i): Biome = getBiome(position.x, position.y, position.z)

    override fun <E : Entity> getEntitiesOfType(type: Class<E>): Collection<E> {
        return world.entityTracker.entitiesInChunkOfType(position, type, null)
    }

    override fun <E : Entity> getEntitiesOfType(type: Class<E>, predicate: Predicate<E>): Collection<E> {
        return world.entityTracker.entitiesInChunkOfType(position, type, predicate)
    }

    private fun lightSectionCount(): Int = sectionCount() + 2

    fun minimumLightSection(): Int = minimumSection() - 1

    fun maximumLightSection(): Int = minimumLightSection() + lightSectionCount()

    override fun height(): Int = world.height()

    override fun minimumBuildHeight(): Int = world.minimumBuildHeight()
}
