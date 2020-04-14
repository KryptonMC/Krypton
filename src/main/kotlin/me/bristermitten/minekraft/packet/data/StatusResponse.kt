package me.bristermitten.minekraft.packet.data

import kotlinx.serialization.Serializable
import me.bristermitten.minekraft.serializers.UUIDSerializer
import java.util.*

@Serializable
data class StatusResponse(
    val version: ServerVersion,
    val players: Players,
    val description: Chat
)

@Serializable
data class ServerVersion(
    val name: String,
    val protocol: Long
)

@Serializable
data class Players(
    val max: Int,
    val online: Int,
    val sample: Set<PlayerInfo> = emptySet()
)

@Serializable
data class PlayerInfo(
    val name: String,
    @Serializable(with = UUIDSerializer::class)
    val id: UUID = UUID.randomUUID()
)


