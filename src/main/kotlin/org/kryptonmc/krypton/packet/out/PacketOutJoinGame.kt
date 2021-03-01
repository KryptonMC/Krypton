package org.kryptonmc.krypton.packet.out

import io.netty.buffer.ByteBuf
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.nbt.ListBinaryTag
import org.kryptonmc.krypton.entity.Gamemode
import org.kryptonmc.krypton.extension.writeNBTCompound
import org.kryptonmc.krypton.extension.writeString
import org.kryptonmc.krypton.extension.writeVarInt
import org.kryptonmc.krypton.packet.state.PlayPacket

class PacketOutJoinGame(
    val entityId: Int,
    val gamemode: Gamemode,
    val maxPlayers: Int,
    val viewDistance: Int = 16
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
            .put("minecraft:dimension_type", CompoundBinaryTag.builder()
                .putString("type", "minecraft:dimension_type")
                .put("value", ListBinaryTag.builder()
                    .add(CompoundBinaryTag.builder()
                        .putString("name", "minecraft:overworld")
                        .putInt("id", 0)
                        .put("element", CompoundBinaryTag.builder()
                            .putByte("piglin_safe", 0b0)
                            .putByte("natural", 0b1)
                            .putFloat("ambient_light", 0.0f)
                            .putString("infiniburn", "minecraft:infiniburn_overworld")
                            .putByte("respawn_anchor_works", 0b0)
                            .putByte("has_skylight", 0b1)
                            .putByte("bed_works", 0b1)
                            .putString("effects", "minecraft:overworld")
                            .putByte("has_raids", 0b1)
                            .putInt("logical_height", 256)
                            .putFloat("coordinate_scale", 1.0f)
                            .putByte("ultrawarm", 0b0)
                            .putByte("has_ceiling", 0b0)
                            .build())
                        .build())
                    .build())
                .build())
            .put("minecraft:worldgen/biome", CompoundBinaryTag.builder()
                .putString("type", "minecraft:worldgen/biome")
                .put("value", ListBinaryTag.builder()
                    .add(CompoundBinaryTag.builder()
                        .putString("name", "minecraft:plains")
                        .putInt("id", 0)
                        .put("element", CompoundBinaryTag.builder()
                            .putString("precipitation", "rain")
                            .put("effects", CompoundBinaryTag.builder()
                                .putInt("sky_color", 7907327)
                                .putInt("water_fog_color", 329011)
                                .putInt("fog_color", 12638463)
                                .putInt("water_color", 4159204)
                                .put("mood_sound", CompoundBinaryTag.builder()
                                    .putInt("tick_delay", 6000)
                                    .putFloat("offset", 0.0f)
                                    .putString("sound", "minecraft:ambient.cave")
                                    .putInt("block_search_extent", 8)
                                    .build())
                                .build())
                            .putFloat("depth", 0.125f)
                            .putFloat("temperature", 0.8f)
                            .putFloat("scale", 0.05f)
                            .putFloat("downfall", 0.4f)
                            .putString("category", "plains")
                            .build())
                        .build())
                    .build())
                .build())
            .build())

        // dimension info
        buf.writeNBTCompound(CompoundBinaryTag.builder()
            .putByte("piglin_safe", 0b0)
            .putByte("natural", 0b1)
            .putFloat("ambient_light", 0.0f)
            .putString("infiniburn", "minecraft:infiniburn_overworld")
            .putByte("respawn_anchor_works", 0b0)
            .putByte("has_skylight", 0b1)
            .putByte("bed_works", 0b1)
            .putString("effects", "minecraft:overworld")
            .putByte("has_raids", 0b1)
            .putInt("logical_height", 256)
            .putFloat("coordinate_scale", 1.0f)
            .putByte("ultrawarm", 0b0)
            .putByte("has_ceiling", 0b0)
            .build())

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