package org.kryptonmc.krypton.entity.entities.data

import net.kyori.adventure.nbt.CompoundBinaryTag
import org.kryptonmc.krypton.api.registry.NamespacedKey
import org.kryptonmc.krypton.api.space.Vector
import org.kryptonmc.krypton.api.world.Gamemode
import org.kryptonmc.krypton.entity.Attribute
import org.kryptonmc.krypton.entity.memory.EmptyBrain
import org.kryptonmc.krypton.packet.out.play.PacketOutJoinGame.Companion.OVERWORLD
import org.kryptonmc.krypton.packet.out.play.entity.PacketOutEntityProperties.Companion.DEFAULT_PLAYER_ATTRIBUTES

// don't even ask
data class PlayerData(
    val sleepTimer: Short = 0,
    val spawnForced: Boolean = false,
    val attributes: List<Attribute> = DEFAULT_PLAYER_ATTRIBUTES,
    val isInvulnerable: Boolean = false,
    val absorptionAmount: Float = 0.0F,
    val abilities: PlayerAbilities = PlayerAbilities(),
    val fallDistance: Float = 0.0F,
    val air: Short = 300,
    val spawnPosition: Vector = Vector.ZERO,
    val spawnAngle: Float = 0.0F,
    val score: Int = 0,
    val position: Vector = Vector.ZERO,
    val previousGamemode: Gamemode? = null,
    val gamemode: Gamemode = Gamemode.SURVIVAL,
    val selectedItemSlot: Int = 0,
    val hurtTime: Short = 0,
    val inventory: List<InventoryItem> = emptyList(),
    val fallFlying: Boolean = false,
    val spawnDimension: NamespacedKey = OVERWORLD,
    val dimension: NamespacedKey = OVERWORLD,
    val health: Float = 1.0F,
    val isOnGround: Boolean = false,
    val rotationX: Float = 0.0F,
    val rotationY: Float = 0.0F
) {

    val brain = EmptyBrain
}

data class PlayerAbilities(
    val canInstantlyBuild: Boolean = false,
    val mayFly: Boolean = false,
    val isInvulnerable: Boolean = false,
    val mayBuild: Boolean = false,
    val isFlying: Boolean = false,
    val walkSpeed: Float = 0.1F,
    val flySpeed: Float = 0.05F
)

data class InventoryItem(
    val slot: Byte,
    val id: NamespacedKey,
    val tag: CompoundBinaryTag = CompoundBinaryTag.empty(),
    val count: Byte
) {

    fun toNBT() = CompoundBinaryTag.builder()
        .putByte("Slot", slot)
        .putString("id", id.toString())
        .apply { if (tag != CompoundBinaryTag.empty()) put("tag", tag) }
        .putByte("Count", count)
        .build()
}