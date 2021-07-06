package org.kryptonmc.krypton.world.block

import net.kyori.adventure.text.Component
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.registry.block.BlockData

class KryptonBlock(
    data: BlockData,
    override val properties: Map<String, String>
) : Block {

    override val key = data.key
    override val id = data.id
    override val stateId = data.stateId
    override val hardness = data.hardness
    override val explosionResistance = data.resistance
    override val friction = data.friction
    override val speedFactor = data.speedFactor
    override val jumpFactor = data.jumpFactor
    override val isAir = data.air
    override val isSolid = data.solid
    override val isLiquid = data.liquid
    override val hasBlockEntity = data.blockEntity
    override val blocksMotion = data.blocksMotion
    override val hasGravity = data.gravity
    override val isFlammable = data.flammable
    override val lightEmission = data.lightEmission
    override val occludes = data.occludes
    override val translation = Component.translatable(data.translationKey)

    init {
        Registries.register(Registries.BLOCK, key, this)
    }

    override fun withProperty(key: String, value: String): Block {
        val properties = properties.toMutableMap().apply { put(key, value) }
        return requireNotNull(KryptonBlockLoader.properties(this.key.asString(), properties)) { "Invalid properties: $key:$value" }
    }

    override fun compareTo(other: Block) = id.compareTo(other.id)
}
