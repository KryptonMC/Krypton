package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import net.kyori.adventure.nbt.CompoundBinaryTag
import org.kryptonmc.krypton.entity.Gamemode
import org.kryptonmc.krypton.extension.writeKey
import org.kryptonmc.krypton.extension.writeNBTCompound
import org.kryptonmc.krypton.extension.writeString
import org.kryptonmc.krypton.extension.writeVarInt
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.registry.NamespacedKey
import org.kryptonmc.krypton.registry.biomes.BiomeRegistry
import org.kryptonmc.krypton.registry.dimensions.DimensionRegistry
import org.kryptonmc.krypton.world.World
import org.kryptonmc.krypton.world.generation.DebugGenerator
import org.kryptonmc.krypton.world.generation.FlatGenerator
import java.nio.ByteBuffer
import java.security.MessageDigest

class PacketOutJoinGame(
    private val entityId: Int,
    private val isHardcore: Boolean = false,
    private val world: World,
    private val gamemode: Gamemode,
    private val dimensions: DimensionRegistry,
    private val biomes: BiomeRegistry,
    private val maxPlayers: Int = 20,
    private val viewDistance: Int = 10
) : PlayPacket(0x24) {

    override fun write(buf: ByteBuf) {
        buf.writeInt(entityId)
        buf.writeBoolean(isHardcore) // is hardcore
        buf.writeByte(gamemode.id) // gamemode
        buf.writeByte(-1) // previous gamemode

        // worlds that exist
        buf.writeVarInt(3)
        buf.writeKey(OVERWORLD)
        buf.writeKey(NETHER)
        buf.writeKey(END)

        // dimension codec (dimension/biome type registry)
        buf.writeNBTCompound(CompoundBinaryTag.builder()
            .put("minecraft:dimension_type", dimensions.toNBT())
            .put("minecraft:worldgen/biome", biomes.toNBT())
            .build())

        // dimension info
        buf.writeNBTCompound(OVERWORLD_NBT)

        val messageDigest = MessageDigest.getInstance("SHA-256")
        val seedBytes = ByteBuffer.allocate(Long.SIZE_BYTES).putLong(world.worldGenSettings.seed).array()
        val hashedSeed = ByteBuffer.wrap(messageDigest.digest(seedBytes)).getLong(0)

        buf.writeKey(OVERWORLD) // world spawning into - for now, this is always overworld
        buf.writeLong(hashedSeed)
        buf.writeVarInt(maxPlayers)
        buf.writeVarInt(viewDistance)

        // TODO: Add gamerules and make these two use them
        buf.writeBoolean(false) // reduced debug info
        buf.writeBoolean(true) // enable respawn screen

        val generator = world.worldGenSettings.dimensions.getValue(NamespacedKey(value = "overworld")).generator
        buf.writeBoolean(generator is DebugGenerator) // is debug world
        buf.writeBoolean(generator is FlatGenerator) // is flat world
    }

    companion object {

        private val OVERWORLD = NamespacedKey(value = "overworld")
        private val NETHER = NamespacedKey(value = "the_nether")
        private val END = NamespacedKey(value = "the_end")
    }
}

private val OVERWORLD_NBT = CompoundBinaryTag.builder()
    .putBoolean("piglin_safe", false)
    .putBoolean("natural", true)
    .putFloat("ambient_light", 0.0f)
    .putString("infiniburn", "minecraft:infiniburn_overworld")
    .putBoolean("respawn_anchor_works", false)
    .putBoolean("has_skylight", true)
    .putBoolean("bed_works", true)
    .putString("effects", "minecraft:overworld")
    .putBoolean("has_raids", true)
    .putInt("logical_height", 256)
    .putDouble("coordinate_scale", 1.0)
    .putBoolean("ultrawarm", false)
    .putBoolean("has_ceiling", false)
    .build()