package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import net.kyori.adventure.nbt.CompoundBinaryTag
import org.kryptonmc.krypton.entity.Gamemode
import org.kryptonmc.krypton.extension.writeNBTCompound
import org.kryptonmc.krypton.extension.writeString
import org.kryptonmc.krypton.extension.writeVarInt
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.registry.biomes.BiomeRegistry
import org.kryptonmc.krypton.registry.dimensions.DimensionRegistry

class PacketOutJoinGame(
    private val entityId: Int,
    private val gamemode: Gamemode,
    private val dimensions: DimensionRegistry,
    private val biomes: BiomeRegistry,
    private val maxPlayers: Int,
    private val viewDistance: Int = 16
) : PlayPacket(0x24) {

    override fun write(buf: ByteBuf) {
        buf.writeInt(entityId) // EID
        buf.writeBoolean(false) // is hardcore
        buf.writeByte(gamemode.id) // gamemode
        buf.writeByte(-1) // previous gamemode
        buf.writeVarInt(1) // size of worlds array
        buf.writeString("minecraft:world")

        // dimension codec (dimension/biome type registry)
        buf.writeNBTCompound(CompoundBinaryTag.builder()
            .put("minecraft:dimension_type", dimensions.toNBT())
            .put("minecraft:worldgen/biome", biomes.toNBT())
            .build())

        // dimension info
        buf.writeNBTCompound(OVERWORLD_NBT)

        buf.writeString("minecraft:world") // world name
        buf.writeLong(11111111) // hashed seed
        buf.writeVarInt(maxPlayers) // max players
        buf.writeVarInt(viewDistance) // view distance
        buf.writeBoolean(false) // reduced debug info
        buf.writeBoolean(true) // enable respawn screen
        buf.writeBoolean(false) // is debug world
        buf.writeBoolean(false) // is flat world
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