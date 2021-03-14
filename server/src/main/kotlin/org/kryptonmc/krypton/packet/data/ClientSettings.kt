package org.kryptonmc.krypton.packet.data

import org.kryptonmc.krypton.api.event.events.play.SkinSettings
import org.kryptonmc.krypton.entity.MainHand

data class ClientSettings(
    val locale: String,
    val viewDistance: Byte,
    val chatMode: ChatMode,
    val chatColors: Boolean,
    val skinSettings: SkinSettings,
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