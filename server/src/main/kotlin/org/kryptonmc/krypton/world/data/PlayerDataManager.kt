package org.kryptonmc.krypton.world.data

import net.kyori.adventure.nbt.*
import net.kyori.adventure.nbt.BinaryTagIO.Compression.GZIP
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import org.kryptonmc.krypton.api.entity.Abilities
import org.kryptonmc.krypton.api.inventory.item.ItemStack
import org.kryptonmc.krypton.api.inventory.item.Material
import org.kryptonmc.krypton.api.inventory.item.meta.ItemMeta
import org.kryptonmc.krypton.api.registry.NamespacedKey
import org.kryptonmc.krypton.api.registry.toNamespacedKey
import org.kryptonmc.krypton.api.world.Gamemode
import org.kryptonmc.krypton.api.world.Location
import org.kryptonmc.krypton.entity.Attribute
import org.kryptonmc.krypton.entity.AttributeKey
import org.kryptonmc.krypton.entity.entities.KryptonPlayer
import org.kryptonmc.krypton.entity.memory.EmptyBrain
import org.kryptonmc.krypton.serializers.serialize
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.NBT_DATA_VERSION
import java.io.File
import java.io.IOException
import java.util.*

class PlayerDataManager(private val folder: File) {

    fun loadAndPopulate(world: KryptonWorld, player: KryptonPlayer) {
        player.world = world

        val playerFile = File(folder, "${player.uuid}.dat")
        if (!playerFile.exists()) {
            playerFile.createNewFile()
            applyDefaults(world, player)
            return
        }

        val nbt = try {
            BinaryTagIO.unlimitedReader().read(playerFile.toPath(), GZIP)
        } catch (exception: IOException) {
            return
        }

        player.attributes = nbt.getList("Attributes").map { attribute ->
            (attribute as CompoundBinaryTag).let {
                Attribute(AttributeKey.fromKey(it.getString("Name").toNamespacedKey()), it.getDouble("Base"))
            }
        }.toSet()

        player.abilities = nbt.getCompound("abilities").let {
            Abilities(
                it.getBoolean("instabuild"),
                it.getBoolean("mayfly"),
                it.getBoolean("invulnerable"),
                it.getBoolean("mayBuild"),
                it.getBoolean("flying"),
                it.getFloat("walkSpeed"),
                it.getFloat("flySpeed")
            )
        }

        val inventoryItems = nbt.getList("Inventory").associate { item ->
            (item as CompoundBinaryTag).let {
                val type = Material.valueOf(it.getString("id").split(":")[1].toUpperCase())
                val slot = it.getByte("Slot")
                val count = it.getByte("Count")
                slot.toInt() to ItemStack(type, count.toInt())
            }
        }
        player.inventory.populate(inventoryItems)

        val position = nbt.getList("Pos").map { (it as DoubleBinaryTag).value() }
        val rotation = nbt.getList("Rotation").map { (it as FloatBinaryTag).value() }

        player.location = Location(world, position[0], position[1], position[2], rotation[0], rotation[1])
        player.oldGamemode = Gamemode.fromId(nbt.getInt("previousPlayerGameType", -1))
        player.gamemode = Gamemode.fromId(nbt.getInt("playerGameType", 0)) ?: Gamemode.SURVIVAL
        player.inventory.heldSlot = nbt.getInt("SelectedItemSlot")
        player.dimension = nbt.getString("Dimension").toNamespacedKey()
        player.isOnGround = nbt.getBoolean("OnGround")
    }

    fun save(player: KryptonPlayer) {
        val playerFile = File(folder, "${player.uuid}.dat").apply { createNewFile() }

        val attributes = player.attributes.map {
            CompoundBinaryTag.builder()
                .putDouble("Base", it.value)
                .putString("Name", it.key.key.toString())
                .build()
        }

        val helmet = player.inventory.helmet
        val chestplate = player.inventory.chestplate
        val leggings = player.inventory.leggings
        val boots = player.inventory.boots
        val offHand = player.inventory.offHand

        val inventory = ((player.inventory.hotbar + player.inventory.main).mapIndexed { index, item ->
            if (item == null) return@mapIndexed null
            item.toNBT(index)
        }.filterNotNull() as MutableList<CompoundBinaryTag>).apply {
            if (helmet != null) add(helmet.toNBT(103))
            if (chestplate != null) add(chestplate.toNBT(102))
            if (leggings != null) add(leggings.toNBT(101))
            if (boots != null) add(boots.toNBT(100))
            if (offHand != null) add(offHand.toNBT(-106))
        }

        BinaryTagIO.writer().write(CompoundBinaryTag.builder()
            .put(EmptyBrain.write())
            .putShort("SleepTimer", 0)
            .putBoolean("SpawnForced", false)
            .put("Attributes", ListBinaryTag.from(attributes))
            .putBoolean("Invulnerable", player.abilities.isInvulnerable)
            .putFloat("AbsorptionAmount", 0F)
            .put("abilities", CompoundBinaryTag.builder()
                .putFloat("walkSpeed", player.abilities.walkSpeed)
                .putBoolean("instabuild", player.abilities.canInstantlyBuild)
                .putBoolean("mayfly", player.abilities.canFly)
                .putBoolean("invulnerable", player.abilities.isInvulnerable)
                .putBoolean("mayBuild", player.abilities.canBuild)
                .putBoolean("flying", player.abilities.isFlying)
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
            .putInt("previousPlayerGameType", player.oldGamemode?.ordinal ?: -1)
            .putInt("DataVersion", NBT_DATA_VERSION)
            .putInt("SelectedItemSlot", player.inventory.heldSlot)
            .putShort("HurtTime", 0)
            .put("Inventory", ListBinaryTag.from(inventory))
            .putBoolean("FallFlying", false)
            .putInt("playerGameType", player.gamemode.ordinal)
            .putString("SpawnDimension", player.dimension.toString())
            .putFloat("Health", 1F)
            .putBoolean("OnGround", player.isOnGround)
            .putString("Dimension", player.dimension.toString())
            .put("Rotation", ListBinaryTag.builder()
                .add(FloatBinaryTag.of(player.location.yaw))
                .add(FloatBinaryTag.of(player.location.pitch))
                .build())
            .build(), playerFile.toPath(), GZIP)
    }

    // this ensures player data is always set, even when the player doesn't have any
    // persisted data
    private fun applyDefaults(world: KryptonWorld, player: KryptonPlayer) {
        player.location = world.spawnLocation
        player.gamemode = world.gamemode
        player.dimension = OVERWORLD

        player.abilities = when (world.gamemode) {
            Gamemode.SURVIVAL, Gamemode.ADVENTURE -> Abilities()
            Gamemode.CREATIVE -> Abilities(isInvulnerable = true, isFlying = true, canInstantlyBuild = true)
            Gamemode.SPECTATOR -> Abilities(isInvulnerable = true, canFly = true, isFlying = true)
        }
    }

    companion object {

        private val OVERWORLD = NamespacedKey(value = "overworld")
    }
}

private fun ItemStack.toNBT(slot: Int) = CompoundBinaryTag.builder()
    .putString("id", type.key.toString())
    .putByte("Slot", slot.toByte())
    .putByte("Count", amount.toByte())
    .apply { if (meta != null) { put("display", meta!!.toNBT()) } }
    .build()

private fun ItemMeta.toNBT() = CompoundBinaryTag.builder()
    .apply { if (displayName != null) putString("Name", GsonComponentSerializer.gson().serialize(displayName!!)) }
    .apply { if (lore.isNotEmpty()) put("Lore", lore.toNBT()) }
    .build()

private fun List<Component>.toNBT() =
    ListBinaryTag.of(BinaryTagTypes.STRING, map { StringBinaryTag.of(GsonComponentSerializer.gson().serialize(it)) })