package org.kryptonmc.krypton.data

import net.kyori.adventure.nbt.*
import net.kyori.adventure.nbt.BinaryTagIO.Compression.GZIP
import org.kryptonmc.krypton.api.registry.toNamespacedKey
import org.kryptonmc.krypton.api.space.Vector
import org.kryptonmc.krypton.api.world.Gamemode
import org.kryptonmc.krypton.entity.Attribute
import org.kryptonmc.krypton.entity.AttributeKey
import org.kryptonmc.krypton.entity.entities.KryptonPlayer
import org.kryptonmc.krypton.entity.entities.data.InventoryItem
import org.kryptonmc.krypton.entity.entities.data.PlayerAbilities
import org.kryptonmc.krypton.entity.entities.data.PlayerData
import org.kryptonmc.krypton.entity.memory.EmptyBrain
import org.kryptonmc.krypton.serializers.serialize
import org.kryptonmc.krypton.world.NBT_DATA_VERSION
import java.io.File
import java.io.IOException
import java.util.*

class PlayerDataManager(private val folder: File) {

    fun load(uuid: UUID): PlayerData? {
        val playerFile = File(folder, "$uuid.dat")
        if (!playerFile.exists()) {
            playerFile.createNewFile()
            return null
        }

        val nbt = try {
            BinaryTagIO.unlimitedReader().read(playerFile.toPath(), GZIP)
        } catch (exception: IOException) {
            return null
        }

        val attributes = nbt.getList("Attributes").map { attribute ->
            (attribute as CompoundBinaryTag).let {
                Attribute(AttributeKey.fromKey(it.getString("Name").toNamespacedKey()), it.getDouble("Base"))
            }
        }.toSet()

        val abilities = nbt.getCompound("abilities").let {
            PlayerAbilities(
                it.getBoolean("instabuild"),
                it.getBoolean("mayfly"),
                it.getBoolean("invulnerable"),
                it.getBoolean("mayBuild"),
                it.getBoolean("flying"),
                it.getFloat("walkSpeed"),
                it.getFloat("flySpeed")
            )
        }

        val inventory = nbt.getList("Inventory").map { item ->
            (item as CompoundBinaryTag).let {
                InventoryItem(
                    it.getByte("Slot"),
                    it.getString("id").toNamespacedKey(),
                    it.getCompound("tag"),
                    it.getByte("Count")
                )
            }
        }

        val position = nbt.getList("Pos").map { (it as DoubleBinaryTag).value() }
        val rotation = nbt.getList("Rotation").map { (it as FloatBinaryTag).value() }

        return PlayerData(
            nbt.getShort("SleepTimer"),
            nbt.getBoolean("SpawnForced"),
            attributes,
            nbt.getBoolean("Invulnerable"),
            nbt.getFloat("AbsorptionAmount"),
            abilities,
            nbt.getFloat("FallDistance"),
            nbt.getShort("Air", 300),
            Vector(nbt.getInt("SpawnX"), nbt.getInt("SpawnY"), nbt.getInt("SpawnZ")),
            nbt.getFloat("SpawnAngle"),
            nbt.getInt("Score"),
            Vector(position[0], position[1], position[2]),
            Gamemode.fromId(nbt.getInt("previousPlayerGameType", -1)),
            Gamemode.fromId(nbt.getInt("playerGameType", 0)) ?: Gamemode.SURVIVAL,
            nbt.getInt("SelectedItemSlot"),
            nbt.getShort("HurtTime"),
            inventory,
            nbt.getBoolean("FallFlying"),
            nbt.getString("SpawnDimension").toNamespacedKey(),
            nbt.getString("Dimension").toNamespacedKey(),
            nbt.getFloat("Health", 1F),
            nbt.getBoolean("OnGround"),
            rotation[1],
            rotation[0]
        )
    }

    fun save(player: KryptonPlayer) {
        val playerFile = File(folder, "${player.uuid}.dat").apply { createNewFile() }

        val attributes = player.attributes.map {
            CompoundBinaryTag.builder()
                .putDouble("Base", it.value)
                .putString("Name", it.key.key.toString())
                .build()
        }

        BinaryTagIO.writer().write(CompoundBinaryTag.builder()
            .put(EmptyBrain.write())
            .putShort("SleepTimer", 0)
            .putBoolean("SpawnForced", false)
            .put("Attributes", ListBinaryTag.from(attributes))
            .putBoolean("Invulnerable", player.abilities.isInvulnerable)
            .putFloat("AbsorptionAmount", 0F)
            .put("abilities", CompoundBinaryTag.builder()
                .putFloat("walkSpeed", 0.1F)
                .putBoolean("instabuild", player.abilities.isCreativeMode)
                .putBoolean("mayfly", player.abilities.canFly)
                .putBoolean("invulnerable", player.abilities.isInvulnerable)
                .putBoolean("mayBuild", player.gamemode != Gamemode.ADVENTURE && player.gamemode != Gamemode.SPECTATOR)
                .putBoolean("flying", player.isFlying)
                .putFloat("flySpeed", player.abilities.flyingSpeed)
                .build())
            .putFloat("FallDistance", 0F)
            .put("UUID", player.uuid.serialize())
            .putInt("SpawnX", 0)
            .putInt("SpawnY", 0)
            .putInt("SpawnZ", 0)
            .putFloat("SpawnAngle", 0F)
            .putShort("Air", 300)
            .putInt("Score", 0)
            .put("Pos", ListBinaryTag.builder()
                .add(DoubleBinaryTag.of(player.location.x))
                .add(DoubleBinaryTag.of(player.location.y))
                .add(DoubleBinaryTag.of(player.location.z))
                .build())
            .putInt("previousPlayerGameType", -1)
            .putInt("DataVersion", NBT_DATA_VERSION)
            .putInt("SelectedItemSlot", 0)
            .putShort("HurtTime", 0)
            .put("Inventory", ListBinaryTag.of(BinaryTagTypes.COMPOUND, player.inventory.map(InventoryItem::toNBT)))
            .putBoolean("FallFlying", false)
            .putInt("playerGameType", player.gamemode.ordinal)
            .putString("SpawnDimension", player.dimension.toString())
            .putFloat("Health", 1F)
            .putBoolean("OnGround", player.isOnGround)
            .putString("Dimension", player.dimension.toString())
            .put("Rotation", ListBinaryTag.builder()
                .add(FloatBinaryTag.of(player.location.pitch))
                .add(FloatBinaryTag.of(player.location.yaw))
                .build())
            .build(), playerFile.toPath(), GZIP)
    }
}