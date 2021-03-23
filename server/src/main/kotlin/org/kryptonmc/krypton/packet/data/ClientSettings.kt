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

enum class ChatMode {

    ENABLED,
    COMMANDS_ONLY,
    HIDDEN;

    companion object {

        fun fromId(id: Int) = values()[id]
    }
}