package me.bristermitten.minekraft.packet.data

import me.bristermitten.minekraft.entity.MainHand
import me.bristermitten.minekraft.entity.metadata.SkinFlags

data class ClientSettings(
    val locale: String,
    val viewDistance: Byte,
    val chatMode: ChatMode,
    val chatColors: Boolean,
    val skinFlags: SkinFlags,
    val mainHand: MainHand
)

enum class ChatMode(val id: Int) {

    ENABLED(0),
    COMMANDS_ONLY(1),
    HIDDEN(2);

    companion object {

        private val VALUES = values()

        fun fromId(id: Int) = VALUES[id]
    }
}