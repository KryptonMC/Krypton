package org.kryptonmc.krypton.packet.data

import org.kryptonmc.krypton.entity.MainHand
import org.kryptonmc.krypton.entity.metadata.SkinFlags

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