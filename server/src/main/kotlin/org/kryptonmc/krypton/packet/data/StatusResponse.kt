package org.kryptonmc.krypton.packet.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.serializers.ComponentSerializer
import org.kryptonmc.krypton.serializers.UUIDSerializer
import java.util.*

/**
 * A serializable class sent as the response to a status request. Specifically, this is serialized
 * to JSON and sent by the [Status response][org.kryptonmc.krypton.packet.out.status.PacketOutStatusResponse]
 * packet.
 *
 * @author Alex Wood
 */
@Serializable
data class StatusResponse(
    val version: ServerVersion,
    val players: Players,
    @Serializable(with = ComponentSerializer::class) val description: Component
)

/**
 * Information about the server's version. This is used by the Notchian client to determine whether
 * we are compatible with it.
 *
 * @author Alex Wood
 */
@Serializable
data class ServerVersion(
    val name: String,
    val protocol: Int
)

/**
 * The players list. This is used by the Notchian client to display the current, maximum and sample
 * players currently on the server.
 *
 * @author Alex Wood
 */
@Serializable
data class Players(
    val max: Int,
    val online: Int,
    val sample: Set<PlayerInfo> = emptySet()
)

/**
 * A player's info, for status
 *
 * @author Alex Wood
 */
@Serializable
data class PlayerInfo(
    val name: String,
    @SerialName("id") @Serializable(with = UUIDSerializer::class) val uuid: UUID
)