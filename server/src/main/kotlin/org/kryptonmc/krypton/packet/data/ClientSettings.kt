package org.kryptonmc.krypton.packet.data

import org.kryptonmc.krypton.api.event.events.play.SkinSettings
import org.kryptonmc.krypton.entity.MainHand

/**
 * Holder for various settings sent by clients. Some of these settings, like [viewDistance],
 * [chatMode], [skinSettings] and [mainHand], we make use of. [locale] and [chatColors] are
 * currently unused, and merely there because the client sends these, and we will make use
 * of them later.
 */
data class ClientSettings(
    val locale: String,
    val viewDistance: Byte,
    val chatMode: ChatMode,
    val chatColors: Boolean,
    val skinSettings: SkinSettings,
    val mainHand: MainHand
)

/**
 * The status of the client's chat mode. Enabled indicates that the client is happy to receive any
 * form of media from any source. Commands only indicates that the client only wishes to receive
 * messages from commands and action bars. Hidden indicates the client only wishes to receive action
 * bars.
 */
enum class ChatMode {

    ENABLED,
    COMMANDS_ONLY,
    HIDDEN
}
