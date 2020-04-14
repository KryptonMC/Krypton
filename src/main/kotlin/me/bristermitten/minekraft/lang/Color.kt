package me.bristermitten.minekraft.lang;

import kotlinx.serialization.*


@Serializable
enum class Color(private val char: Char, private val clientName: String)
{
    BLACK('0', "black"),
    DARK_BLUE('1', "dark_blue"),
    DARK_GREEN('2', "dark_green"),
    DARK_CYAN('3', "dark_aqua"),
    DARK_RED('4', "dark_red"),
    PURPLE('5', "dark_purple"),
    GOLD('6', "gold"),
    GRAY('7', "gray"),
    DARK_GRAY('8', "dark_gray"),
    BLUE('9', "blue"),
    GREEN('a', "green"),
    CYAN('b', "aqua"),
    RED('c', "red"),
    PINK('d', "light_purple"),
    YELLOW('e', "yellow"),
    WHITE('f', "white");

    fun toClientString() = clientName
    fun toChatString() = "$COLOR_CHAR$char"

    @Serializer(forClass = Color::class)
    companion object : KSerializer<Color>
    {

        override val descriptor: SerialDescriptor = SerialDescriptor("Color", PrimitiveKind.STRING)

        override fun serialize(encoder: Encoder, value: Color)
        {
            encoder.encodeString(value.toClientString())
        }

        override fun deserialize(decoder: Decoder): Color
        {
            return fromClientString(decoder.decodeString())
        }

        fun fromClientString(clientString: String): Color
        {
            return valueOf(clientString.toUpperCase())
        }
    }
}
