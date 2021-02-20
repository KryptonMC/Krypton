package org.kryptonmc.krypton.packet.data

import kotlinx.serialization.Serializable
import org.kryptonmc.krypton.lang.Color
import org.kryptonmc.krypton.lang.Effect

@Serializable
@Deprecated("We now use Komponent for this")
data class Chat(
    val text: String,
    val bold: Boolean = false,
    val italic: Boolean = false,
    val underlined: Boolean = false,
    val strikethrough: Boolean = false,
    val obfuscated: Boolean = false,
    val color: Color = Color.WHITE,
    val extra: Collection<Chat>? = null
) {

    fun toChatString(): String {
        return buildString {
            if (color != Color.WHITE)
                append(color.toChatString())

            if (bold) append(Effect.BOLD.toChatString())
            if (italic) append(Effect.ITALIC.toChatString())
            if (underlined) append(Effect.UNDERLINED.toChatString())
            if (strikethrough) append(Effect.STRIKETHROUGH.toChatString())
            if (obfuscated) append(Effect.OBFUSCATED.toChatString())

            append(text)
            extra?.forEach {
                append(it.toChatString())
            }
        }
    }
}