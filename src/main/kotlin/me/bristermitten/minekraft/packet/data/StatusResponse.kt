package me.bristermitten.minekraft.packet.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.bardy.komponent.Component
import me.bristermitten.minekraft.serializers.UUIDSerializer
import java.util.*

@Serializable
data class StatusResponse(
    val version: ServerVersion,
    val players: Players,
    @Serializable(with = Component.Companion::class) val description: Component
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
    @SerialName("id") @Serializable(with = UUIDSerializer::class) val uuid: UUID
)